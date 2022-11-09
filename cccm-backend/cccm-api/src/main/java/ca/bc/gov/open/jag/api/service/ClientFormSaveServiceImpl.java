package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.CloneFormRequest;
import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.model.data.FormInput;
import ca.bc.gov.open.jag.api.model.service.CloneConfig;
import ca.bc.gov.open.jag.api.model.service.CloneForm;
import ca.bc.gov.open.jag.api.model.service.DeleteRequest;
import ca.bc.gov.open.jag.api.util.JwtUtils;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CreateFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.UpdateFormInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ca.bc.gov.open.jag.api.Keys.*;

@RequestScoped
public class ClientFormSaveServiceImpl implements ClientFormSaveService {

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    private CloneConfig cloneConfig;

    public ClientFormSaveServiceImpl(ObjectMapper objectMapper) throws IOException {
        objectMapper.findAndRegisterModules();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        cloneConfig = objectMapper.readValue(loader.getResourceAsStream("configs/clone_config.json"), CloneConfig.class);

    }

    @Override
    public BigDecimal createCRNA(CreateFormInput createFormInput, BigDecimal locationId) {

        List<CodeTable> codes = obridgeClientService.getFormTypes(CRNA_FORM_TYPE);
        return createForm(createFormInput, locationId, new BigDecimal(codes.get(0).getCode()));

    }

    @Override
    public BigDecimal createSARA(CreateFormInput createFormInput, BigDecimal locationId) {

        BigDecimal formSARATypeId = getCode(SARA_FORM_TYPE, obridgeClientService.getFormTypes(SARA_FORM_TYPE));
        BigDecimal formCRNATypeId = getCode(CRNA_FORM_TYPE, obridgeClientService.getFormTypes(CRNA_FORM_TYPE));
        if (createFormInput.getLinkedClientFormId() == null) {
            createFormInput.setLinkedClientFormId(createForm(createFormInput, locationId, formCRNATypeId));
        }

        return createForm(createFormInput, locationId, formSARATypeId);

    }

    @Override
    public void updateForm(BigDecimal clientFormId, String updateFormInput) {
        obridgeClientService.updateForm(clientFormId, updateFormInput);
    }

    @Override
    public BigDecimal completeForm(UpdateFormInput completeFormInput, BigDecimal locationId, Boolean hasOverride, String idirId) {

        ClientFormSummary clientFormSummary = obridgeClientService.getClientFormSummary(completeFormInput.getClientNumber(), completeFormInput.getClientFormId());

        if (!hasOverride && !JwtUtils.stripUserName(idirId).equalsIgnoreCase(clientFormSummary.getCreatedBy())) {
            throw new CCCMException("User who created the form can only delete", CCCMErrorCode.VALIDATIONERROR);
        }

        FormInput formInput = new FormInput();
        formInput.setLocationId(locationId);
        formInput.setClientFormId(completeFormInput.getClientFormId());
        formInput.setFormLevelComments(completeFormInput.getFormLevelComments());
        formInput.setPlanSummary(completeFormInput.getPlanSummary());
        formInput.setSourcesContacted(completeFormInput.getSourcesContacted());
        formInput.setClientNumber(completeFormInput.getClientNumber());
        formInput.setCompletionDate(LocalDate.now());

        BigDecimal result = obridgeClientService.createForm(formInput);

        if (completeFormInput.getLinkedClientFormId() != null) {

            FormInput childFormInput = new FormInput();
            childFormInput.setLocationId(locationId);
            childFormInput.setClientFormId(completeFormInput.getLinkedClientFormId());
            childFormInput.setFormLevelComments(completeFormInput.getFormLevelComments());
            childFormInput.setPlanSummary(completeFormInput.getPlanSummary());
            childFormInput.setSourcesContacted(completeFormInput.getSourcesContacted());
            childFormInput.setClientNumber(completeFormInput.getClientNumber());
            childFormInput.setCompletionDate(LocalDate.now());

            obridgeClientService.createForm(childFormInput);

        }

        return result;

    }

    @Override
    public void editForm(UpdateFormInput updateFormInput, BigDecimal locationId, Boolean hasOverride, String idirId) {

        ClientFormSummary clientFormSummary = obridgeClientService.getClientFormSummary(updateFormInput.getClientNumber(), updateFormInput.getClientFormId());

        if (!hasOverride && !JwtUtils.stripUserName(idirId).equalsIgnoreCase(clientFormSummary.getCreatedBy())) {
            throw new CCCMException("User who created the form can only delete", CCCMErrorCode.VALIDATIONERROR);
        }
        //TODO: Update both instances
        FormInput formInput = new FormInput();
        formInput.setLocationId(locationId);
        formInput.setClientFormId(updateFormInput.getClientFormId());
        formInput.setFormLevelComments(updateFormInput.getFormLevelComments());
        formInput.setPlanSummary(updateFormInput.getPlanSummary());
        formInput.setSourcesContacted(updateFormInput.getSourcesContacted());
        formInput.setClientNumber(updateFormInput.getClientNumber());
        formInput.setCompletionDate(null);

        obridgeClientService.createForm(formInput);

        if (updateFormInput.getLinkedClientFormId() != null) {

            FormInput childFormInput = new FormInput();
            childFormInput.setLocationId(locationId);
            childFormInput.setClientFormId(updateFormInput.getLinkedClientFormId());
            childFormInput.setFormLevelComments(updateFormInput.getFormLevelComments());
            childFormInput.setPlanSummary(updateFormInput.getPlanSummary());
            childFormInput.setSourcesContacted(updateFormInput.getSourcesContacted());
            childFormInput.setClientNumber(updateFormInput.getClientNumber());
            childFormInput.setCompletionDate(LocalDate.now());

            obridgeClientService.createForm(childFormInput);

        }

    }

    @Override
    public void linkForm(UpdateFormInput linkFormInput, BigDecimal locationId) {

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

        ClientFormSummary clientFormSummary = obridgeClientService.getClientFormSummary(clientNum, clientFormId);

        if (!hasOverride && !JwtUtils.stripUserName(idirId).equalsIgnoreCase(clientFormSummary.getCreatedBy())) {
            throw new CCCMException("User who created the form can only delete", CCCMErrorCode.VALIDATIONERROR);
        }

        obridgeClientService.deleteForm(new DeleteRequest(clientFormId, locationId, clientNum, JwtUtils.stripUserName(idirId)));

    }


    @Override
    public BigDecimal cloneClientForm(CloneFormRequest cloneFormRequest, String idirId) {

        //Get Form Details
        ClientFormSummary clientFormSummary = obridgeClientService.getClientFormSummary(cloneFormRequest.getClientNumber(), cloneFormRequest.getClientFormId());

        if (!clientFormSummary.getMostRecent() || !validateFormType(clientFormSummary.getFormTypeId(), clientFormSummary.getModule())) {
            throw new CCCMException("Cannot clone form that is not most recent or mismatch type", CCCMErrorCode.CLONEVALIDATIONERROR);
        }

        BigDecimal clientFormId = cloneFormAndAnswers(clientFormSummary, cloneFormRequest, null);

        //Add related form when cloning
        if (clientFormSummary.getRelatedClientFormId() != null) {
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
