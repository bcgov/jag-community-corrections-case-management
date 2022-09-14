package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.service.FormRequest;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.FormsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class FormsApiImpl implements FormsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(FormsApiImpl.class));

    @Inject
    FormDataService formDataService;

    @Inject
    ClientDataService clientDataService;



    @Override
    @Transactional
    @RolesAllowed("form-add")
    public BigDecimal createNewFormUsingPOST(CreateFormInput createFormInput) {
       return clientDataService.addClientForm(createFormInput);
    }

    @Override
    @Transactional
//    @RolesAllowed("form-view")
    public String getFormAsJSONUsingGET(BigDecimal clientFormId, String clientNumber, Boolean includeOptionValues) {
        return clientDataService.getClientFormJSON( clientFormId,clientNumber, includeOptionValues);
    }



    @Override
    @Transactional
    @RolesAllowed("form-update")
    public FormQuestionAnswer addQuestionAnswer(BigDecimal formId, @Valid FormQuestionAnswer formQuestionAnswer) {

        logger.info("Add question answer received");

        if (formQuestionAnswer.getFormQuestionId() == null) {
            throw new CCCMException("Required ID not present", CCCMErrorCode.VALIDATIONERROR);
        }

        return null;


    }

    @Override
    @Transactional
    @RolesAllowed("form-delete")
    public void deleteForm(BigDecimal formId) {

        logger.warning("Form delete not implemented");

        //TODO implement when ready
    }


    @Override
    @RolesAllowed("form-view")
    public FormDetails getFormByType(String formType) {

        logger.info("Get form type request received");

        return formDataService.formRequest(new FormRequest(null, formType));

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public FormList getForms() {

        logger.info("Get forms request received");

        FormList formList = new FormList();

        formList.setItems(Collections.singletonList(formDataService.formRequest(new FormRequest(BigDecimal.ONE, null))));

        return formList;

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
    public FormQuestionAnswer updateQuestionAnswer(BigDecimal formId, @Valid FormQuestionAnswer formQuestionAnswer) {

        logger.info("Update question answer request received");

        if (formQuestionAnswer.getFormAnswerId() == null || formQuestionAnswer.getFormQuestionId() == null) {
            throw new CCCMException("Required ID's not present", CCCMErrorCode.VALIDATIONERROR);
        }

        return null;

    }




    @Override
    @Transactional
    @RolesAllowed("form-view")
    public FormSearchList getClientForms(String clientNum, Boolean currentPeriod, String formTypeCd) {
        logger.info("Client form search");
        return formDataService.formSearch(clientNum, currentPeriod, formTypeCd);
    }

    @Override
    public FormDetails getFormById(BigDecimal formId, Boolean includeAnswers) {
        return formDataService.getForm(formId, includeAnswers);
    }

    @Override
    public List<FormSummary> getFormSummaries(String module, Boolean latestOnly) {
        return formDataService.getFormSummaries(module, latestOnly);
    }
}
