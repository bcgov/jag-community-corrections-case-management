package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.Keys;
import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.*;
import ca.bc.gov.open.jag.api.model.service.CloneConfig;
import ca.bc.gov.open.jag.api.model.service.CloneForm;
import ca.bc.gov.open.jag.api.model.service.DeleteRequest;
import ca.bc.gov.open.jag.api.model.service.UpdateForm;
import ca.bc.gov.open.jag.api.util.JwtUtils;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CreateFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LinkFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static ca.bc.gov.open.jag.api.Keys.*;
import static ca.bc.gov.open.jag.api.util.FormUtils.ratingToInteger;

@RequestScoped
public class ClientFormSaveServiceImpl implements ClientFormSaveService {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(ClientFormSaveServiceImpl.class));

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    UserDataService userDataService;

    @Inject
    ValidationService validationService;

    private CloneConfig cloneConfig;

    public ClientFormSaveServiceImpl(ObjectMapper objectMapper) throws IOException {
        objectMapper.findAndRegisterModules();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        cloneConfig = objectMapper.readValue(loader.getResourceAsStream("configs/clone_config.json"), CloneConfig.class);

    }

    @Override
    public BigDecimal createCRNA(CreateFormInput createFormInput, BigDecimal locationId) {

        logger.debug("Create CRNA form {} location {}", createFormInput, locationId);

        List<CodeTable> codes = obridgeClientService.getFormTypes(CRNA_FORM_TYPE);
        return createForm(createFormInput, locationId, new BigDecimal(codes.get(0).getCode()));

    }

    @Override
    public BigDecimal createSARA(CreateFormInput createFormInput, BigDecimal locationId) {

        logger.debug("Create SARA form {} location {}", createFormInput, locationId);

        BigDecimal formSARATypeId = getCode(SARA_FORM_TYPE, obridgeClientService.getFormTypes(SARA_FORM_TYPE));
        BigDecimal formCRNATypeId = getCode(CRNA_FORM_TYPE, obridgeClientService.getFormTypes(CRNA_FORM_TYPE));
        BigDecimal CRNAFormId = null;
        if (createFormInput.getLinkedClientFormId() == null) {
            logger.info("Create Linked CRNA Form");
            CRNAFormId = createForm(createFormInput, locationId, formCRNATypeId);
            createFormInput.setLinkedClientFormId(CRNAFormId);
        }

        logger.info("Create SARA Form");
		// CBCCCM-770, always return the SARA form instance ID
		return createForm(createFormInput, locationId, formSARATypeId);
    }

    @Override
    public BigDecimal createACUTE(CreateFormInput createFormInput, BigDecimal locationId) {

        logger.debug("Create ACUTE form {} location {}", createFormInput, locationId);

        List<CodeTable> codes = obridgeClientService.getFormTypes(ACUTE_FORM_TYPE);
        return createForm(createFormInput, locationId, new BigDecimal(codes.get(0).getCode()));

    }

    @Override
    public BigDecimal createStatic99r(CreateFormInput createFormInput, BigDecimal locationId) {

        logger.debug("Create Static99r form {} location {}", createFormInput, locationId);

        List<CodeTable> codes = obridgeClientService.getFormTypes(STATIC99R_FORM_TYPE);
        return createForm(createFormInput, locationId, new BigDecimal(codes.get(0).getCode()));

    }

    @Override
    public BigDecimal createStable(CreateFormInput createFormInput, BigDecimal locationId) {

        logger.debug("Create Stable form {} location {}", createFormInput, locationId);

        List<CodeTable> codes = obridgeClientService.getFormTypes(STABLE_FORM_TYPE);
        return createForm(createFormInput, locationId, new BigDecimal(codes.get(0).getCode()));

    }

    @Override
    public BigDecimal createSOOverall(CreateFormInput createFormInput, BigDecimal locationId) {

        logger.debug("Create Overall form {} location {}", createFormInput, locationId);

        List<CodeTable> codes = obridgeClientService.getFormTypes(OVERALL_FORM_TYPE);
        return createForm(createFormInput, locationId, new BigDecimal(codes.get(0).getCode()));

    }

    @Override
    public BigDecimal createCMRP(CreateFormInput createFormInput, BigDecimal locationId) {

        logger.debug("Create CMRP form {} location {}", createFormInput, locationId);

        List<CodeTable> codes = obridgeClientService.getFormTypes(CUSTODY_CMRP_FORM_TYPE);
        return createForm(createFormInput, locationId, new BigDecimal(codes.get(0).getCode()));

    }

    @Override
    public void updateForm(BigDecimal clientFormId, String updateFormInput) {

        logger.debug("Update form {} formId {}", updateFormInput, clientFormId);

        obridgeClientService.updateForm(clientFormId, updateFormInput);

    }

    @Override
    public void editForm(UpdateForm updateForm, String location) {

        logger.debug("Edit form {}", updateForm);
        boolean requiresNew = false;
        ClientFormSummary clientFormSummary = obridgeClientService.getClientFormSummary(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId(), new BigDecimal(location));
        if (!updateForm.getHasOverride()
                && !JwtUtils.stripUserName(updateForm.getIdirId()).equalsIgnoreCase(clientFormSummary.getCreatedByIdir())) {
            throw new CCCMException("User who created the form can only edit or complete", CCCMErrorCode.VALIDATIONERROR);
        }

        if (updateForm.getComplete()) {
            if (clientFormSummary.getModule().equalsIgnoreCase(CRNA_FORM_TYPE)) {
                ValidationResult result = validationService.validateCRNA(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId(), new BigDecimal(location)), updateForm.getInterventionKeys(), clientFormSummary.getCasePlanNotRequiredYn());
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("CRNA form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }

                if (updateForm.getUpdateFormInput().getLinkedClientFormId() != null) {
                    ValidationResult saraResult = validationService.validateSARA(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getLinkedClientFormId(), new BigDecimal(location)));
                    if (!saraResult.getErrors().isEmpty()) {
                        throw new CCCMException("SARA form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, saraResult);
                    }
                }
            }

            if (clientFormSummary.getModule().equalsIgnoreCase(SARA_FORM_TYPE)) {
                ValidationResult result = validationService.validateSARA(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId(), new BigDecimal(location)));
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("SARA form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }

                if (updateForm.getUpdateFormInput().getLinkedClientFormId() != null) {
                    ValidationResult crnaResult = validationService.validateCRNA(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getLinkedClientFormId(), new BigDecimal(location)), updateForm.getInterventionKeys(), clientFormSummary.getCasePlanNotRequiredYn());
                    if (!crnaResult.getErrors().isEmpty()) {
                        throw new CCCMException("CRNA form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, crnaResult);
                    }
                }
            }

            if (clientFormSummary.getModule().equalsIgnoreCase(ACUTE_FORM_TYPE)) {
                ValidationResult result = validationService.validateACUTE(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId(), new BigDecimal(location)));
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("ACUTE form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }
                requiresNew = requiresNewForm(updateForm, clientFormSummary, location);
            }

            if (clientFormSummary.getModule().equalsIgnoreCase(STATIC99R_FORM_TYPE)) {
                ValidationResult result = validationService.validateStatic99r(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId(), new BigDecimal(location)));
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("Static-99R form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }
                requiresNew = requiresNewForm(updateForm, clientFormSummary, location);
            }

            if (clientFormSummary.getModule().equalsIgnoreCase(STABLE_FORM_TYPE)) {
                //TODO: Add validation rule to "An Acute and a Static have been completed in the current period of supervision."
                ValidationResult result = validationService.validateStable(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId(), new BigDecimal(location)));
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("Stable form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }
            }

            if (clientFormSummary.getModule().equalsIgnoreCase(OVERALL_FORM_TYPE)) {
                ValidationResult result = validationService.validateSOOverall(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId(), new BigDecimal(location)), obridgeClientService.getClientFormInterventionsForCasePlan(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId(), true, new BigDecimal(location)), clientFormSummary.getCasePlanNotRequiredYn());
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("Overall form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }
            }

            if (clientFormSummary.getModule().equalsIgnoreCase(CUSTODY_CMRP_FORM_TYPE)) {
                ClientDates clientDates = obridgeClientService.getClientDates(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getIdirId(), updateForm.getLocationId());
                ValidationResult result = validationService.validateCMRP(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId(), new BigDecimal(location)), clientDates, updateForm.getInterventionKeys());
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("CMRP form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }
            }

        }

        CompleteFormInput formInput = new CompleteFormInput();

        String oracelId = userDataService.getOracleId(updateForm.getIdirId());
        formInput.setClientFormId(updateForm.getUpdateFormInput().getClientFormId());
        formInput.setClientNumber(updateForm.getUpdateFormInput().getClientNumber());
        formInput.setCompleteYn((updateForm.getComplete() ? YES : NO));
        formInput.setLocationId(updateForm.getLocationId());
        //Set form type and module
        formInput.setFormType(clientFormSummary.getModule());
        formInput.setFormTypeId(clientFormSummary.getFormTypeId());
        //Set case plan omissable
        formInput.setCasePlanOmissable(clientFormSummary.getCasePlanNotRequiredYn());
        if (updateForm.getComplete()) {

            ClientFormAnswers existingAnswers = obridgeClientService.getClientFormAnswersObject(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId());
            //These can probably be deleted. The logs have been removed.
            formInput.setFormLevelComments(existingAnswers.getFormComments());
            formInput.setPlanSummary(existingAnswers.getPlanSummary());
            formInput.setSourcesContacted(existingAnswers.getSourcesContacted());
            if (updateForm.getUpdateFormInput().getLinkedClientFormId() != null) {

                ClientFormAnswers childAnswers = obridgeClientService.getClientFormAnswersObject(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getLinkedClientFormId());
                String supervisionRating;
                String formType;
                //CRNA and SARA could be completed so reverse is applied
                if (clientFormSummary.getModule().equals(CRNA_FORM_TYPE)) {
                    formType = MessageFormat.format("{0}-{1}", clientFormSummary.getModule(), SARA_FORM_TYPE);
                    supervisionRating = (ratingToInteger(getAnswerCodeByKey(childAnswers, SUPERVISION_RATING)) > ratingToInteger(getAnswerCodeByKey(existingAnswers, SUPERVISION_RATING)) ? getAnswerDescByKey(childAnswers, SUPERVISION_RATING) : getAnswerDescByKey(existingAnswers, SUPERVISION_RATING));
                } else {
                    formType = MessageFormat.format("{0}-{1}", CRNA_FORM_TYPE, clientFormSummary.getModule());
                    supervisionRating = (ratingToInteger(getAnswerCodeByKey(existingAnswers, SUPERVISION_RATING)) > ratingToInteger(getAnswerCodeByKey(childAnswers, SUPERVISION_RATING)) ? getAnswerDescByKey(existingAnswers, SUPERVISION_RATING) : getAnswerDescByKey(childAnswers, SUPERVISION_RATING));
                }
                formInput.setFormType(formType);
                formInput.setOverallSupervision(MessageFormat.format("Supervision Rating: {0}", supervisionRating));

            } else {
                formInput.setFormType(clientFormSummary.getModule());
                if (clientFormSummary.getModule().equals(CUSTODY_CMRP_FORM_TYPE)) {
                    formInput.setOverallSupervision(buildCMRPLogComments(existingAnswers));
                } else {
                    formInput.setOverallSupervision(MessageFormat.format("Supervision Rating: {0}", getAnswerDescByKey(existingAnswers, SUPERVISION_RATING)));
                }
            }



        }
        formInput.setOracleId(oracelId);
        logger.info("Complete Form");
        obridgeClientService.setCompletion(formInput);

        if (updateForm.getUpdateFormInput().getLinkedClientFormId() != null) {

            CompleteFormInput childFormInput = new CompleteFormInput();
            childFormInput.setClientFormId(updateForm.getUpdateFormInput().getLinkedClientFormId());
            childFormInput.setClientNumber(updateForm.getUpdateFormInput().getClientNumber());
            childFormInput.setCompleteYn((updateForm.getComplete() ? YES : NO));
            childFormInput.setLocationId(updateForm.getLocationId());
            childFormInput.setOracleId(oracelId);
            if (updateForm.getComplete()) {

                ClientFormSummary childClientFormSummary = obridgeClientService.getClientFormSummary(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId(), new BigDecimal(location));
                childFormInput.setFormTypeId(childClientFormSummary.getFormTypeId());

            }
            logger.info("Complete Child Form");
            obridgeClientService.setCompletion(childFormInput);

        }

        if (requiresNew) {

            CreateFormInput createFormInput = new CreateFormInput();
            createFormInput.setClientNumber(updateForm.getUpdateFormInput().getClientNumber());
            createSOOverall(createFormInput, updateForm.getLocationId());

        }

    }

    @Override
    public void linkForm(LinkFormInput linkFormInput, BigDecimal locationId) {

        logger.debug("Link form {} location {}", linkFormInput, locationId);

        FormInput formInput = new FormInput();
        formInput.setLocationId(locationId);
        formInput.setClientFormId(linkFormInput.getClientFormId());
        formInput.setFormLevelComments(linkFormInput.getFormLevelComments());
        formInput.setPlanSummary(linkFormInput.getPlanSummary());
        formInput.setSourcesContacted(linkFormInput.getSourcesContacted());
        formInput.setLinkedClientFormId(linkFormInput.getLinkedClientFormId());

        obridgeClientService.createForm(formInput);

    }

    @Override
    public void deleteForm(BigDecimal clientFormId, String clientNum, BigDecimal locationId, String idirId, Boolean hasOverride) {

        logger.debug("Delete form {} client {} location {} user {} hasOverride {}", clientFormId, clientNum, locationId, idirId, hasOverride);

        ClientFormSummary clientFormSummary = obridgeClientService.getClientFormSummary(clientNum, clientFormId, locationId);

        if (!hasOverride && !JwtUtils.stripUserName(idirId).equalsIgnoreCase(clientFormSummary.getCreatedByIdir())) {
            throw new CCCMException("User who created the form can only delete", CCCMErrorCode.VALIDATIONERROR);
        }

        obridgeClientService.deleteForm(new DeleteRequest(clientFormId, locationId, clientNum, JwtUtils.stripUserName(idirId)));

    }


    @Override
    public BigDecimal cloneClientForm(CloneFormRequest cloneFormRequest, String idirId, String location) {

        logger.debug("Clone request {} user {}", cloneFormRequest, idirId);

        //Get Form Details
        ClientFormSummary clientFormSummary = obridgeClientService.getClientFormSummary(cloneFormRequest.getClientNumber(), cloneFormRequest.getClientFormId(), new BigDecimal(location));

        if (!clientFormSummary.getModule().equals(CUSTODY_CMRP_FORM_TYPE) && !cloneFormRequest.getHasOverride() && !JwtUtils.stripUserName(idirId).equalsIgnoreCase(clientFormSummary.getCreatedByIdir())) {
            throw new CCCMException("User who created the form can only clone", CCCMErrorCode.VALIDATIONERROR);
        } else if (!clientFormSummary.getMostRecent() || !validateFormType(clientFormSummary.getFormTypeId(), clientFormSummary.getModule())) {
            throw new CCCMException("Cannot clone form that is not most recent or mismatch type", CCCMErrorCode.CLONEVALIDATIONERROR);
        }

        BigDecimal clientFormId = cloneFormAndAnswers(clientFormSummary, cloneFormRequest, null, location);

        //Add related form when cloning
        if (clientFormSummary.getRelatedClientFormId() != null) {
            logger.info("Linking cloned form");
            ClientFormSummary relatedClientFormSummary = obridgeClientService.getClientFormSummary(cloneFormRequest.getClientNumber(), clientFormSummary.getRelatedClientFormId(), new BigDecimal(location));
            cloneFormAndAnswers(relatedClientFormSummary, cloneFormRequest, clientFormId, location);
        }

        return clientFormId;

    }

    private BigDecimal cloneFormAndAnswers(ClientFormSummary clientFormSummary, CloneFormRequest cloneFormRequest, BigDecimal parentId, String location) {

        //Create top level form
        FormInput formInput = new FormInput();
        formInput.setLocationId(clientFormSummary.getLocationId());
        formInput.setFormTypeId(clientFormSummary.getFormTypeId());
        formInput.setClientNumber(cloneFormRequest.getClientNumber());
        if (parentId != null) {
            formInput.setLinkedClientFormId(parentId);
        }
        BigDecimal clientFormId = obridgeClientService.createForm(formInput);

        //Get Answers
        String answers = obridgeClientService.getClientFormAnswers(cloneFormRequest.getClientNumber(), clientFormSummary.getId(), new BigDecimal(location));
        //Insert Answers Use Clone Config for ignore
        String strippedAnswers = stripAnswers(answers, cloneConfig.getForms().stream().filter(cloneForm -> cloneForm.getFormType().equalsIgnoreCase(clientFormSummary.getModule())).findFirst().get());
        obridgeClientService.saveClientFormAnswers(cloneFormRequest.getClientNumber(), clientFormId, strippedAnswers, true, false, new BigDecimal(location));

        return clientFormId;

    }

    private BigDecimal createForm(CreateFormInput createFormInput, BigDecimal locationId, BigDecimal formTypeId) {

        FormInput formInput = new FormInput();
        formInput.setClientNumber(createFormInput.getClientNumber());
        formInput.setFormTypeId(formTypeId);
        formInput.setLocationId(locationId);
        formInput.setLinkedClientFormId(createFormInput.getLinkedClientFormId());

        return obridgeClientService.createForm(formInput);
    }

    private BigDecimal getCode(String codeValue, List<CodeTable> codes) {

       Optional<CodeTable> code = codes
               .stream()
               .filter(codeTable -> codeTable.getValue().equalsIgnoreCase(codeValue))
               .findFirst();

       return code.map(codeTable -> new BigDecimal(codeTable.getCode())).orElse(null);

    }

    private Boolean requiresNewForm(UpdateForm updateForm, ClientFormSummary currentForm, String location) {

        List<ClientFormSummary> formSummaryList = obridgeClientService.getClientForms(updateForm.getUpdateFormInput().getClientNumber(), true, currentForm.getModule(), new BigDecimal(location));

        Optional<ClientFormSummary> lastForm = formSummaryList.stream()
                .filter(summary -> summary != null && summary.getCompletedDate() != null)
                .max(Comparator.comparing(ClientFormSummary::getCompletedDate));

        return (lastForm.isPresent() && !StringUtils.equals(currentForm.getSupervisionRating(), lastForm.get().getSupervisionRating()));

    }

    private String stripAnswers(String answers, CloneForm cloneForm) {

        JSONObject answerJson = null;
        JSONObject outerData = new JSONObject(answers);
        if (outerData.has(OUTER_DATA_ELEMENT)) {
            answerJson = outerData.getJSONObject(OUTER_DATA_ELEMENT);
        } else {
            answerJson = outerData;
        }

        for (String key: cloneForm.getIgnoreKeys()) {
            answerJson.remove(key);
        }
        List<String> removeKeys = new ArrayList<>();
        //Strip ids
        for (String key: answerJson.keySet()) {

            if (key.contains(INTERVENTION_DATAGRID)) {
                String[] keyParts = key.split("_");

                for (int i = 0; i <= answerJson.getJSONArray(key).length() - 1; i++) {
                    answerJson.getJSONArray(key).getJSONObject(i).remove(MessageFormat.format(INTERVENTION_KEY_PATTERN,  keyParts[0], INTERVENTION_ID));
                }
            } else if (key.contains("ID")) {
                removeKeys.add(key);
            }
        }

        for (String removeKey: removeKeys) {
            answerJson.remove(removeKey);
        }

        return answerJson.toString();

    }

    private Optional<Answer> getAnswerByKey(ClientFormAnswers clientFormAnswers, String key) {

        if (clientFormAnswers == null || clientFormAnswers.getAnswers().isEmpty()) return Optional.empty();

        return clientFormAnswers.getAnswers().stream().filter(
                answer -> answer.getKey().equals(key)
        ).findFirst();

    }

    private String getAnswerDescByKey(ClientFormAnswers clientFormAnswers, String key) {

        if (clientFormAnswers == null || clientFormAnswers.getAnswers().isEmpty()) return "";

        Optional<Answer> rating = getAnswerByKey(clientFormAnswers, key);

        if (rating.isPresent()) {
            return rating.get().getDescription();
        }

        return "";

    }

    private String getAnswerCodeByKey(ClientFormAnswers clientFormAnswers, String key) {

        if (clientFormAnswers == null || clientFormAnswers.getAnswers().isEmpty()) return "";

        Optional<Answer> rating = getAnswerByKey(clientFormAnswers, key);

        if (rating.isPresent()) {
            return rating.get().getText();
        }

        return "";

    }


    private Boolean validateFormType(BigDecimal formTypeId, String formType) {

        List<CodeTable> codes = obridgeClientService.getFormTypes(formType);

        Optional<CodeTable> code = codes.stream().filter(codeTable -> new BigDecimal(codeTable.getCode()).equals(formTypeId)).findFirst();

        return code.isPresent();

    }

    private String buildCMRPLogComments(ClientFormAnswers clientFormAnswers) {

        Optional<Answer> proposedResidence = getAnswerByKey(clientFormAnswers, CMRP_PROPOSED_RESIDENCE);

        return  StringUtils.truncate(
                MessageFormat.format("Hub Location: {0} {1}Proposed Residence: {2} {3}Assessment Comments: {4}",
                        getAnswerDescByKey(clientFormAnswers, CMRP_HUB_LOCATION),
                        System.lineSeparator(),
                        proposedResidence.isPresent() ? proposedResidence.get().getText() : "",
                        System.lineSeparator(),
                        (clientFormAnswers.getFormComments() == null ? "" : clientFormAnswers.getFormComments())
                ), 4000);

    }

}
