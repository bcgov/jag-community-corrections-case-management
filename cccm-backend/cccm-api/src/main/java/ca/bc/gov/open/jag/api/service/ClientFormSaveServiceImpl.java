package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.data.FormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CompleteFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CreateFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.UpdateFormInput;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;

@RequestScoped
public class ClientFormSaveServiceImpl implements ClientFormSaveService {

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Override
    public BigDecimal createCRNA(CreateFormInput createFormInput, BigDecimal locationId) {

        return createForm(createFormInput, locationId, BigDecimal.ONE);

    }

    @Override
    public BigDecimal createSARA(CreateFormInput createFormInput, BigDecimal locationId) {

        //TODO Get Form Type Id's
        BigDecimal formSARATypeId = BigDecimal.ONE;
        BigDecimal formCRNATypeId = BigDecimal.ONE;
        if (createFormInput.getLinkedClientFormId() == null) {
            createFormInput.setLinkedClientFormId(createForm(createFormInput, locationId, formCRNATypeId));
        }

        return createForm(createFormInput, locationId, formSARATypeId);

    }

    @Override
    public BigDecimal updateForm(UpdateFormInput updateFormInput, BigDecimal locationId) {

        FormInput formInput = new FormInput();
        formInput.setLocationId(locationId);
        formInput.setClientFormId(updateFormInput.getClientFormId());
        formInput.setPlanSummary(updateFormInput.getPlanSummary());
        formInput.setFormLevelComments(updateFormInput.getReAssessmentComments());
        formInput.setSourcesContacted(updateFormInput.getSourcesContacted());

        return obridgeClientService.createForm(formInput);
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

    private BigDecimal createForm(CreateFormInput createFormInput, BigDecimal locationId, BigDecimal formTypeId) {

        FormInput formInput = new FormInput();
        formInput.setClientNumber(createFormInput.getClientNumber());
        formInput.setFormTypeId(formTypeId);
        formInput.setLocationId(locationId);
        formInput.setLinkedClientFormId(createFormInput.getLinkedClientFormId());

        return obridgeClientService.createForm(formInput);
    }

}
