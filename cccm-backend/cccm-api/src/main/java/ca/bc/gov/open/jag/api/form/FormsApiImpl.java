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
import java.math.BigDecimal;
import java.util.Collections;
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

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public BigDecimal createNewFormUsingPOST(CreateFormInput createFormInput) {
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
    public String getClientFormAnswersSummaryJSONUsingGET(BigDecimal clientFormId, String clientNumber) {
        return clientDataService.getClientFormAnswersSummary(clientNumber, clientFormId);
    }

    @Override
    @Transactional
    @RolesAllowed("form-update")
    public FormQuestionAnswer addQuestionAnswer(BigDecimal formId, String xLocationId, @Valid FormQuestionAnswer formQuestionAnswer) {

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
    @Transactional
    @RolesAllowed("form-update")
    public FormQuestionAnswer updateQuestionAnswer(BigDecimal formId, String xLocationId, @Valid FormQuestionAnswer formQuestionAnswer) {

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

    public String getClientFormAnswersForSectionAndQuestionUsingGET(BigDecimal clientFormId, String clientNumber, Integer questionSequence, Integer sectionSequence) {
        return clientDataService.getClientFormAnswersForSectionAndQuestion(clientNumber, clientFormId, sectionSequence, questionSequence);
    }

    @Override
    @RolesAllowed("form-view")

    public String getClientFormAnswersForSectionUsingGET(BigDecimal clientFormId, String clientNumber, Integer sectionSequence) {
        return clientDataService.getClientFormAnswersForSection(clientNumber, clientFormId, sectionSequence);
    }


    @Override
    public List<Comment> getClientCommentsUsingPOST(String csNumber, ClientSearchInput searchInput, String xLocationId) {
        return clientDataService.getClientFormComments(csNumber, searchInput);
    }



    @Override
    @RolesAllowed("form-view")
    public List<Intervention> getClientInterventionsUsingPOST(String csNumber, ClientSearchInput searchInput) {
        return clientDataService.getClientFormInterventions(csNumber, searchInput);
    }

    @Override
    @RolesAllowed("form-view")
    public List<Responsivity> getClientResponsivitiesUsingPOST(String csNumber, ClientSearchInput searchInput) {
        return clientDataService.getClientFormResponsivities(csNumber, searchInput);
    }
}
