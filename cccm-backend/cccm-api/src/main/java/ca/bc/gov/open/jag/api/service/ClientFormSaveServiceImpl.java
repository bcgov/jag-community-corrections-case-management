package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.model.data.FormInput;
import ca.bc.gov.open.jag.api.model.service.DeleteRequest;
import ca.bc.gov.open.jag.api.util.JwtUtils;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CompleteFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CreateFormInput;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
    public BigDecimal completeForm(CompleteFormInput completeFormInput, BigDecimal locationId) {

        //TODO: Validation, Save Answers
        FormInput formInput = new FormInput();
        formInput.setLocationId(locationId);
        formInput.setClientFormId(completeFormInput.getClientFormId());
        formInput.setCompletionDate(LocalDate.now());

        return obridgeClientService.createForm(formInput);

    }


    @Override
    public void deleteForm(BigDecimal clientFormId, String clientNum, BigDecimal locationId, String idirId) {

        obridgeClientService.deleteForm(new DeleteRequest(clientFormId, locationId, clientNum, JwtUtils.stripUserName(idirId)));

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

}
