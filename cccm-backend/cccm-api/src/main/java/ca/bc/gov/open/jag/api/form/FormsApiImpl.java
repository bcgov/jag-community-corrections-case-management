package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.service.FormRequest;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.FormsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class FormsApiImpl implements FormsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(FormsApiImpl.class));

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
    @RolesAllowed("form-add")
    public BigDecimal createNewFormUsingPOST(CreateFormInput createFormInput, String xLocationId) {
        createFormInput.setLocation(xLocationId);
        return clientDataService.addClientForm(createFormInput);
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
    @Transactional
    @RolesAllowed("form-update")
    public FormQuestionAnswer addQuestionAnswer(BigDecimal formId, String xLocationId,
            @Valid FormQuestionAnswer formQuestionAnswer) {
        logger.info("Add question answer received");
        if (formQuestionAnswer.getFormQuestionId() == null) {
            throw new CCCMException("Required ID not present", CCCMErrorCode.VALIDATIONERROR);
        }
        return null;
    }

    @Override
    @RolesAllowed("form-view")
    public FormDetails getFormByType(String xLocationId, String formType) {
        logger.info("Get form type request received");
        return formDataService.formRequest(new FormRequest(null, formType));
    }

    @Override
    @Transactional
    @RolesAllowed("form-update")
    public FormQuestionAnswer updateQuestionAnswer(BigDecimal formId, String xLocationId,
            @Valid FormQuestionAnswer formQuestionAnswer) {
        logger.info("Update question answer request received");
        if (formQuestionAnswer.getFormAnswerId() == null || formQuestionAnswer.getFormQuestionId() == null) {
            throw new CCCMException("Required ID's not present", CCCMErrorCode.VALIDATIONERROR);
        }
        return null;
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
    public List<Comment> searchClientCommentsUsingPOST(ClientSearchInput searchInput) {
        return clientDataService.searchClientFormComments(searchInput);
    }

    @Override
    @RolesAllowed("form-view")
    public List<Intervention> searchClientInterventionsUsingPOST(ClientSearchInput searchInput) {
        return clientDataService.searchClientFormInterventions(searchInput);
    }

    @Override
    @RolesAllowed("form-view")
    public List<Responsivity> searchClientResponsivitiesUsingPOST(ClientSearchInput searchInput) {
        return clientDataService.searchClientFormResponsivities(searchInput);
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
    public String getClientFormMetaJson(String csNumber, BigDecimal clientFormId, String xLocationId) {
        return clientDataService.getClientFormMetaJson(csNumber, clientFormId);
    }
}
