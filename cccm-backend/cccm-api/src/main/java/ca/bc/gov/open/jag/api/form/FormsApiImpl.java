package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.FormsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class FormsApiImpl implements FormsApi {

    @Inject
    FormDataService formDataService;

    @Inject
    ClientDataService clientDataService;


    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getClientFormAnswersUsingGET(BigDecimal clientFormId, String clientNumber) {
        return clientDataService.getClientFormAnswers(clientNumber, clientFormId);
    }


    @Override
    @Transactional
    @RolesAllowed("form-update")
    public String saveClientFormAnswersUsingPUT(BigDecimal clientFormId, String clientNumber, Boolean loadLatestValues, String answerPayload) {
        if ( loadLatestValues == null) {
            loadLatestValues = Boolean.FALSE;
        }
        return clientDataService.saveClientFormAnswers(clientNumber,clientFormId, answerPayload,loadLatestValues );
    }

    @Override
    @Transactional
    @RolesAllowed("form-update")

    public void deleteInterventionsExceptUsingPUT(BigDecimal clientFormId, String clientNumber, String updatePayload) {
        clientDataService.deleteInterventionsExcept(clientNumber,clientFormId, updatePayload);
    }

//    @Override
//    @Transactional
//    @RolesAllowed("form-add")
//    public BigDecimal createNewFormUsingPOST(String xLocationId, CreateFormInput createFormInput) {
//       return clientDataService.addClientForm(createFormInput);
//    }

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public BigDecimal createNewFormUsingPOST(CreateFormInput createFormInput, String xLocationId) {
        createFormInput.setLocation(xLocationId);
        return clientDataService.addClientForm(createFormInput);
    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getFormAsJSONUsingGET(BigDecimal clientFormId, String clientNumber, Boolean includeOptionValues) {
        return clientDataService.getClientFormJSON( clientFormId,clientNumber, includeOptionValues);
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
    @RolesAllowed("form-view")
    public ClientFormSummary getClientFormSummaryUsingGET(BigDecimal clientFormId, String clientNumber) {
        return clientDataService.getClientFormSummary(clientFormId, clientNumber);
    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getClientFormAnswersSummaryJSONUsingGET(BigDecimal clientFormId, String clientNumber) {
        return clientDataService.getClientFormAnswersSummary(clientNumber, clientFormId);
    }



//    @Override
//    @Transactional
//    @RolesAllowed("form-update")
//    public FormDetails updateForm(@Valid FormDetails formDetails) {
//
//        logger.warning("Form update not implemented");
//
//        return formDetails;
//
//    }

    @Override
    @RolesAllowed("form-view")
    public List<FormSummary> getFormSummaries(String module, Boolean latestOnly) {
        return formDataService.getFormSummaries(module, latestOnly);
    }

    @Override
    @RolesAllowed("form-view")
    public String getClientFormAnswersForSectionAndQuestionUsingGET(BigDecimal clientFormId, String clientNumber, Integer questionSequence, Integer sectionSequence) {
        return clientDataService.getClientFormAnswersForSectionAndQuestion(clientNumber, clientFormId, sectionSequence, questionSequence);
    }

    @Override
    @RolesAllowed("form-view")
    public String getClientFormAnswersForSectionUsingGET(BigDecimal clientFormId, String clientNumber, Integer sectionSequence) {
        return clientDataService.getClientFormAnswersForSection(clientNumber, clientFormId, sectionSequence);
    }


    @Override
    @RolesAllowed("form-view")
    public List<Comment> searchClientCommentsUsingPOST( ClientSearchInput searchInput) {
        return clientDataService.searchClientFormComments( searchInput);
    }


    @Override
    @RolesAllowed("form-view")
    public List<Intervention> searchClientInterventionsUsingPOST( ClientSearchInput searchInput) {
        return clientDataService.searchClientFormInterventions( searchInput);
    }

    @Override
    @RolesAllowed("form-view")
    public List<Responsivity> searchClientResponsivitiesUsingPOST( ClientSearchInput searchInput) {
        return clientDataService.searchClientFormResponsivities( searchInput);
    }
}
