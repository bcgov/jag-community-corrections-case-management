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

@ApplicationScoped
public class FormsApiServiceImpl implements FormsApi {

    @Inject
    DataService dataService;

    @Override
    @Transactional
    public FormDetails addForm(@Valid FormDetails formDetails) {
        return formDetails;
    }

    @Override
    @Transactional
    public void deleteForm(BigDecimal formId) {
        //TODO implement when ready
    }

    @Override
    @Transactional
    public FormDetails getForm(BigDecimal formId) {
        return dataService.getFormById(BigDecimal.ONE);
    }

    @Override
    @Transactional
    public FormList getForms() {

        FormList formList = new FormList();

        formList.setItems(Collections.singletonList(dataService.getFormById(BigDecimal.ONE)));

        return formList;

    }

    @Override
    @Transactional
    public FormDetails updateForm(@Valid FormDetails formDetails) {
        return formDetails;
    }

}
