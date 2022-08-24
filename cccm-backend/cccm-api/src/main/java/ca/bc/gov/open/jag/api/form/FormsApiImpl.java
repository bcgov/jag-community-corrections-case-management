package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.service.FormRequest;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.FormsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormQuestionAnswer;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSearchList;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class FormsApiImpl implements FormsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(FormsApiImpl.class));

    @Inject
    FormDataService formDataService;

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public FormDetails addForm(String formDetails) {

        logger.warning("Form add not implemented");
        logger.log(Level.INFO, formDetails);
        return new FormDetails();

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
    public FormDetails getFormById(BigDecimal formId) {

        logger.info("Get form id request received");

        return formDataService.formRequest(new FormRequest(formId, null));

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

    @Override
    @Transactional
    @RolesAllowed("form-update")
    public FormDetails updateForm(@Valid FormDetails formDetails) {

        logger.warning("Form update not implemented");

        return formDetails;

    }

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
    public FormSearchList getFormSearch(@NotNull String clientNum, @NotNull Boolean currentPeriod, String formTypeCd) {

        return formDataService.formSearch(clientNum, currentPeriod, formTypeCd);

    }

}
