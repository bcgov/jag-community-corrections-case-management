package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.CloneFormRequest;
import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.model.data.CompleteFormInput;
import ca.bc.gov.open.jag.api.model.data.FormInput;
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
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static ca.bc.gov.open.jag.api.Keys.*;

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
        if (createFormInput.getLinkedClientFormId() == null) {
            logger.info("Create Linked CRNA Form");
            createFormInput.setLinkedClientFormId(createForm(createFormInput, locationId, formCRNATypeId));
        }

        logger.info("Create SARA Form");
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
    public BigDecimal createOverall(CreateFormInput createFormInput, BigDecimal locationId) {

        logger.debug("Create Overall form {} location {}", createFormInput, locationId);

        List<CodeTable> codes = obridgeClientService.getFormTypes(OVERALL_FORM_TYPE);
        return createForm(createFormInput, locationId, new BigDecimal(codes.get(0).getCode()));

    }

    @Override
    public void updateForm(BigDecimal clientFormId, String updateFormInput) {

        logger.debug("Update form {} formId {}", updateFormInput, clientFormId);

        obridgeClientService.updateForm(clientFormId, updateFormInput);

    }

    @Override
    public void editForm(UpdateForm updateForm) {

        logger.debug("Edit form {}", updateForm);
        Boolean requiresNew = false;
        ClientFormSummary clientFormSummary = obridgeClientService.getClientFormSummary(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId());

        if (!updateForm.getHasOverride() && !JwtUtils.stripUserName(updateForm.getIdirId()).equalsIgnoreCase(clientFormSummary.getCreatedBy())) {
            throw new CCCMException("User who created the form can only edit or complete", CCCMErrorCode.VALIDATIONERROR);
        }

        if (updateForm.getComplete()) {
            if (clientFormSummary.getModule().equalsIgnoreCase(CRNA_FORM_TYPE)) {
                ValidationResult result = validationService.validateCRNA(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId()));
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("CRNA form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }

                if (updateForm.getUpdateFormInput().getLinkedClientFormId() != null) {
                    ValidationResult saraResult = validationService.validateSARA(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId()));
                    if (!saraResult.getErrors().isEmpty()) {
                        throw new CCCMException("SARA form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, saraResult);
                    }
                }
            }

            if (clientFormSummary.getModule().equalsIgnoreCase(SARA_FORM_TYPE)) {
                ValidationResult result = validationService.validateSARA(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId()));
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("SARA form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }

                if (updateForm.getUpdateFormInput().getLinkedClientFormId() != null) {
                    ValidationResult crnaResult = validationService.validateCRNA(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId()));
                    if (!crnaResult.getErrors().isEmpty()) {
                        throw new CCCMException("CRNA form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, crnaResult);
                    }
                }
            }

            if (clientFormSummary.getModule().equalsIgnoreCase(ACUTE_FORM_TYPE)) {
                ValidationResult result = validationService.validateACUTE(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId()));
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("ACUTE form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }
                requiresNew = requiresNewForm(updateForm, clientFormSummary);
            }

            if (clientFormSummary.getModule().equalsIgnoreCase(STATIC99R_FORM_TYPE)) {
                ValidationResult result = validationService.validateStatic99r(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId()));
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("Static99r form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }
                requiresNew = requiresNewForm(updateForm, clientFormSummary);
            }

            if (clientFormSummary.getModule().equalsIgnoreCase(STABLE_FORM_TYPE)) {
                ValidationResult result = validationService.validateStable(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId()));
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("Stable form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }
            }

            if (clientFormSummary.getModule().equalsIgnoreCase(OVERALL_FORM_TYPE)) {
                ValidationResult result = validationService.validateOverall(obridgeClientService.getClientFormAnswers(updateForm.getUpdateFormInput().getClientNumber(), updateForm.getUpdateFormInput().getClientFormId()));
                if (!result.getErrors().isEmpty()) {
                    throw new CCCMException("Overall form validation failed:", CCCMErrorCode.VALIDATIONERRORWITHRESULT, result);
                }
            }

        }

        CompleteFormInput formInput = new CompleteFormInput();

        String oracelId = userDataService.getOracleId(updateForm.getIdirId());

        formInput.setClientFormId(updateForm.getUpdateFormInput().getClientFormId());
        formInput.setClientNumber(updateForm.getUpdateFormInput().getClientNumber());
        formInput.setCompleteYn((updateForm.getComplete() ? YES : NO));
        formInput.setOracleId(oracelId);
        logger.info("Complete Form");
        obridgeClientService.setCompletion(formInput);

        if (updateForm.getUpdateFormInput().getLinkedClientFormId() != null) {

            CompleteFormInput childFormInput = new CompleteFormInput();
            childFormInput.setClientFormId(updateForm.getUpdateFormInput().getLinkedClientFormId());
            childFormInput.setClientNumber(updateForm.getUpdateFormInput().getClientNumber());
            childFormInput.setCompleteYn((updateForm.getComplete() ? YES : NO));
            childFormInput.setOracleId(oracelId);
            logger.info("Complete Child Form");
            obridgeClientService.setCompletion(childFormInput);

        }

        if (requiresNew) {

            CreateFormInput createFormInput = new CreateFormInput();
            createFormInput.setClientNumber(updateForm.getUpdateFormInput().getClientNumber());
            if (clientFormSummary.getModule().equalsIgnoreCase(STATIC99R_FORM_TYPE)) {
                createStatic99r(createFormInput, updateForm.getLocationId());
            } else if (clientFormSummary.getModule().equalsIgnoreCase(ACUTE_FORM_TYPE)) {
                createACUTE(createFormInput, updateForm.getLocationId());
            }

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

        ClientFormSummary clientFormSummary = obridgeClientService.getClientFormSummary(clientNum, clientFormId);

        if (!hasOverride && !JwtUtils.stripUserName(idirId).equalsIgnoreCase(clientFormSummary.getCreatedBy())) {
            throw new CCCMException("User who created the form can only delete", CCCMErrorCode.VALIDATIONERROR);
        }

        obridgeClientService.deleteForm(new DeleteRequest(clientFormId, locationId, clientNum, JwtUtils.stripUserName(idirId)));

    }


    @Override
    public BigDecimal cloneClientForm(CloneFormRequest cloneFormRequest, String idirId) {

        logger.debug("Clone request {} user {}", cloneFormRequest, idirId);

        //Get Form Details
        ClientFormSummary clientFormSummary = obridgeClientService.getClientFormSummary(cloneFormRequest.getClientNumber(), cloneFormRequest.getClientFormId());

        if (!cloneFormRequest.getHasOverride() && !JwtUtils.stripUserName(idirId).equalsIgnoreCase(clientFormSummary.getCreatedBy())) {
            throw new CCCMException("User who created the form can only clone", CCCMErrorCode.VALIDATIONERROR);
        } else if (!clientFormSummary.getMostRecent() || !validateFormType(clientFormSummary.getFormTypeId(), clientFormSummary.getModule())) {
            throw new CCCMException("Cannot clone form that is not most recent or mismatch type", CCCMErrorCode.CLONEVALIDATIONERROR);
        }

        BigDecimal clientFormId = cloneFormAndAnswers(clientFormSummary, cloneFormRequest, null);

        //Add related form when cloning
        if (clientFormSummary.getRelatedClientFormId() != null) {
            logger.info("Linking cloned form");
            ClientFormSummary relatedClientFormSummary = obridgeClientService.getClientFormSummary(cloneFormRequest.getClientNumber(), clientFormSummary.getRelatedClientFormId());
            cloneFormAndAnswers(relatedClientFormSummary, cloneFormRequest, clientFormId);
        }

        return clientFormId;

    }

    private BigDecimal cloneFormAndAnswers(ClientFormSummary clientFormSummary, CloneFormRequest cloneFormRequest, BigDecimal parentId) {

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
        String answers = obridgeClientService.getClientFormAnswers(cloneFormRequest.getClientNumber(), clientFormSummary.getId());
        //Insert Answers Use Clone Config for ignore
        String strippedAnswers = stripAnswers(answers, cloneConfig.getForms().stream().filter(cloneForm -> cloneForm.getFormType().equalsIgnoreCase(clientFormSummary.getModule())).findFirst().get());
        obridgeClientService.saveClientFormAnswers(cloneFormRequest.getClientNumber(), clientFormId, strippedAnswers, false);

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

    private Boolean requiresNewForm(UpdateForm updateForm, ClientFormSummary currentForm) {

        List<ClientFormSummary> formSummaryList = obridgeClientService.getClientForms(updateForm.getUpdateFormInput().getClientNumber(), true, currentForm.getModule());

        Optional<ClientFormSummary> lastForm = formSummaryList.stream()
                .max(Comparator.comparing(ClientFormSummary::getCompletedDate));

        return (lastForm.isPresent() && !currentForm.getSupervisionRating().equals(lastForm.get().getSupervisionRating()));

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

        return answerJson.toString();

    }

    private Boolean validateFormType(BigDecimal formTypeId, String formType) {

        List<CodeTable> codes = obridgeClientService.getFormTypes(formType);

        Optional<CodeTable> code = codes.stream().filter(codeTable -> new BigDecimal(codeTable.getCode()).equals(formTypeId)).findFirst();

        return code.isPresent();

    }

}
