package ca.bc.gov.open.jag.api.service;

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

import static ca.bc.gov.open.jag.api.Keys.CRNA_FORM_TYPE;
import static ca.bc.gov.open.jag.api.Keys.SARA_FORM_TYPE;

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

        List<CodeTable> codes = obridgeClientService.getFormTypes("");

        BigDecimal formSARATypeId = getCode(SARA_FORM_TYPE, codes);
        BigDecimal formCRNATypeId = getCode(CRNA_FORM_TYPE, codes);
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
    public BigDecimal completeForm(UpdateFormInput completeFormInput, BigDecimal locationId) {

        FormInput formInput = new FormInput();
        formInput.setLocationId(locationId);
        formInput.setClientFormId(completeFormInput.getClientFormId());
        formInput.setFormLevelComments(completeFormInput.getFormLevelComments());
        formInput.setPlanSummary(completeFormInput.getPlanSummary());
        formInput.setSourcesContacted(completeFormInput.getSourcesContacted());
        formInput.setCompletionDate(LocalDate.now());

        return obridgeClientService.createForm(formInput);

    }

    @Override
    public void editForm(UpdateFormInput updateFormInput, BigDecimal locationId) {

        FormInput formInput = new FormInput();
        formInput.setLocationId(locationId);
        formInput.setClientFormId(updateFormInput.getClientFormId());
        formInput.setFormLevelComments(updateFormInput.getFormLevelComments());
        formInput.setPlanSummary(updateFormInput.getPlanSummary());
        formInput.setSourcesContacted(updateFormInput.getSourcesContacted());
        formInput.setCompletionDate(null);

        obridgeClientService.createForm(formInput);

    }

    @Override
    public void deleteForm(BigDecimal clientFormId, String clientNum, BigDecimal locationId, String idirId) {

        obridgeClientService.deleteForm(new DeleteRequest(clientFormId, locationId, clientNum, JwtUtils.stripUserName(idirId)));

    }


    @Override
    public BigDecimal cloneClientForm(CloneFormRequest cloneFormRequest, String idirId) {

        //Get Form Details
        ClientFormSummary clientFormSummary = obridgeClientService.getClientFormSummary(cloneFormRequest.getClientNumber(), cloneFormRequest.getClientFormId());
        //Create top level form
        FormInput formInput = new FormInput();
        formInput.setLocationId(cloneFormRequest.getLocationId());
        formInput.setFormTypeId(clientFormSummary.getFormTypeId());
        formInput.setClientNumber(cloneFormRequest.getClientNumber());
        BigDecimal clientFormId = obridgeClientService.createForm(formInput);

        //If it is a SARA should we create a linked form?

        //Get Answers
        String answers = obridgeClientService.getClientFormAnswers(cloneFormRequest.getClientNumber(), cloneFormRequest.getClientFormId());
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

        JSONObject answerJson = new JSONObject(answers);

        for (String key: cloneForm.getIgnoreKeys()) {
            answerJson.remove(key);
        }

        return answerJson.toString();

    }

}
