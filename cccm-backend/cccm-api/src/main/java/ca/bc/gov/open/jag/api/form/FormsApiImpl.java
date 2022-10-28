package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.model.data.CloneFormRequest;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.FormsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@RequestScoped
public class FormsApiImpl implements FormsApi {
    
    @Inject
    FormDataService formDataService;

    @Inject
    ClientDataService clientDataService;

    @Inject
    ClientFormSaveService clientFormSaveService;

    @Inject
    ValidationService validationService;


    @Inject
    @Claim(standard = Claims.preferred_username)
    String username;

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getClientFormAnswersUsingGET(BigDecimal clientFormId, String clientNumber) {
        return clientDataService.getClientFormAnswers(clientNumber, clientFormId);
    }

    @Override
    @Transactional
    @RolesAllowed("form-update")
    public String saveClientFormAnswersUsingPUT(BigDecimal clientFormId, String clientNumber, Boolean loadLatestValues,
            String answerPayload) {
        if (loadLatestValues == null) {
            loadLatestValues = Boolean.FALSE;
        }
        return clientDataService.saveClientFormAnswers(clientNumber, clientFormId, answerPayload, loadLatestValues);
    }

    @Override
    @Transactional
    @RolesAllowed("form-update")

    public void deleteInterventionsExceptUsingPUT(BigDecimal clientFormId, String clientNumber, String updatePayload) {
        clientDataService.deleteInterventionsExcept(clientNumber, clientFormId, updatePayload);
    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getFormAsJSONUsingGET(BigDecimal clientFormId, String clientNumber, Boolean includeOptionValues) {
        return clientDataService.getClientFormJSON(clientFormId, clientNumber, includeOptionValues);
    }

    @Override
    @RolesAllowed("form-view")
    public String getDecoratorContentUsingGET(String identifier) {
        return formDataService.getFormDecorator(identifier);
    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public List<ClientFormSummary> clientFormSearchUsingGET(String csNumber, Boolean currentPeriod, String formTypeCd) {
        return clientDataService.clientFormSearch(csNumber, currentPeriod, formTypeCd);
    }

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public BigDecimal cloneClientForm(String xLocationId, @Valid CloneForm cloneForm) {
        return clientFormSaveService.cloneClientForm(new CloneFormRequest(cloneForm.getClientNumber(), cloneForm.getClientFormId(), new BigDecimal(xLocationId)), username);
    }

    @Override
    @RolesAllowed("form-add")
    public BigDecimal completeForm(@Valid @NotNull UpdateFormInput createFormInput, String xLocationId) {
        return clientFormSaveService.completeForm(createFormInput, new BigDecimal(xLocationId));
    }

    @Override
    @RolesAllowed("form-add")
    public void editForm(@Valid @NotNull UpdateFormInput updateFormInput, String xLocationId) {
        clientFormSaveService.editForm(updateFormInput, new BigDecimal(xLocationId));
    }

    @Override
    @RolesAllowed("form-add")
    public BigDecimal createCrnaForm(@Valid @NotNull CreateFormInput createFormInput, String xLocationId) {

        return clientFormSaveService.createCRNA(createFormInput, new BigDecimal(xLocationId));

    }

    @Override
    @RolesAllowed("form-add")
    public BigDecimal createSaraForm(@Valid @NotNull CreateFormInput createFormInput, String xLocationId) {
        return clientFormSaveService.createSARA(createFormInput, new BigDecimal(xLocationId));
    }

    @Override
    @RolesAllowed("form-delete")
    public void deleteClientForm(BigDecimal clientFormId, String clientNum, String xLocationId) {
        clientFormSaveService.deleteForm(clientFormId, clientNum, new BigDecimal(xLocationId), username);
    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public ClientFormSummary getClientFormSummaryUsingGET(BigDecimal clientFormId, String clientNumber) {
        return clientDataService.getClientFormSummary(clientFormId, clientNumber);
    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getClientFormAnswersSummaryJSONUsingGET(BigDecimal clientFormId, String clientNumber, Boolean includeLinkedForm) {
        return clientDataService.getClientFormAnswersSummary(clientNumber, clientFormId, includeLinkedForm);
    }

    @Override
    @RolesAllowed("form-view")
    public List<FormSummary> getFormSummaries(String module, Boolean latestOnly) {
        return formDataService.getFormSummaries(module, latestOnly);
    }

    @Override
    @RolesAllowed("form-view")
    public String getClientFormAnswersForSectionAndQuestionUsingGET(BigDecimal clientFormId, String clientNumber,
            Integer questionSequence, Integer sectionSequence) {
        return clientDataService.getClientFormAnswersForSectionAndQuestion(clientNumber, clientFormId, sectionSequence,
                questionSequence);
    }

    @Override
    @RolesAllowed("form-view")
    public String getClientFormAnswersForSectionUsingGET(BigDecimal clientFormId, String clientNumber,
            Integer sectionSequence) {
        return clientDataService.getClientFormAnswersForSection(clientNumber, clientFormId, sectionSequence);
    }

    @Override
    @RolesAllowed("form-view")
    public List<Intervention> searchClientInterventionsUsingPOST(ClientSearchInput searchInput) {
        return clientDataService.searchClientFormInterventions(searchInput);
    }

    @Override
    @RolesAllowed("form-view")
    public List<Comment> searchClientCommentsUsingPOST( ClientSearchInput searchInput) {
        return clientDataService.searchClientFormComments( searchInput);
    }

    @Override
    @RolesAllowed("form-view")
    public List<Responsivity> searchClientResponsivitiesUsingPOST(ClientSearchInput searchInput) {
        return clientDataService.searchClientFormResponsivities(searchInput);
    }

    @Override
    @RolesAllowed("form-update")
    public void upcertResponsivities(BigDecimal clientFormId, String payload) {
        clientDataService.upcertResponsivities(clientFormId, payload);
    }

    @Override
    @RolesAllowed("form-update")
    public void updateForm(BigDecimal clientFormId, String createFormInput, String xLocationId) {
        clientFormSaveService.updateForm(clientFormId, createFormInput);
    }

    @Override
    @RolesAllowed("form-view")
    public String getClientFormIntervention(String csNumber, BigDecimal clientFormId, Boolean includeLinkedForm, String xLocationId) {
        return clientDataService.getClientFormIntervetionForCasePlan(csNumber, clientFormId, includeLinkedForm);
    }

    @Override
    @RolesAllowed("form-update")
    public void updateSourcesContacted(BigDecimal clientFormId, String sourcesContacted, String xLocationId) {
        clientDataService.updateSourcesContacted(clientFormId, sourcesContacted);
    }

    @Override
    @RolesAllowed("form-view")
    public ValidationResult validateCRNAForm(@Valid @NotNull String body) {
        return validationService.validateCRNA(body);
    }

    @Override
    @RolesAllowed("form-view")
    public ValidationResult validateSARAForm(@Valid @NotNull String body) {
        return validationService.validateSARA(body);
    }

    @Override
    @RolesAllowed("form-view")
    public String getClientFormMetaJson(String csNumber, BigDecimal clientFormId, String xLocationId) {
        return clientDataService.getClientFormMetaJson(csNumber, clientFormId);
    }
}
