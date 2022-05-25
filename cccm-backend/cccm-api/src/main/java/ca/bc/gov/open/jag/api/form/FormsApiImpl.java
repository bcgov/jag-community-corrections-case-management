package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.service.DataService;
import ca.bc.gov.open.jag.cccm.api.openapi.FormsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.logging.Logger;

@ApplicationScoped
public class FormsApiImpl implements FormsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(FormsApiImpl.class));

    @Inject
    DataService dataService;

    @Override
    @Transactional
    public FormDetails addForm(@Valid FormDetails formDetails) {

        logger.warning("Form add not implemented");

        return formDetails;

    }

    @Override
    @Transactional
    public void deleteForm(BigDecimal formId) {

        logger.warning("Form delete not implemented");

        //TODO implement when ready
    }

    @Override
    @Transactional
    public FormDetails getForm(BigDecimal formId) {

        logger.info("Get form request received");

        return dataService.getFormById(formId);

    }

    @Override
    @Transactional
    public FormList getForms() {

        logger.info("Get forms request received");

        FormList formList = new FormList();

        formList.setItems(Collections.singletonList(dataService.getFormById(BigDecimal.ONE)));

        return formList;

    }

    @Override
    @Transactional
    public FormDetails updateForm(@Valid FormDetails formDetails) {

        logger.warning("Form update not implemented");

        return formDetails;

    }

}
