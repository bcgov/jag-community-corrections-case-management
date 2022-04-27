package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.cccm.api.openapi.FormsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormList;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import java.math.BigDecimal;

@ApplicationScoped
public class FormsApiServiceImpl implements FormsApi {

    @Override
    public FormDetails addForm(@Valid FormDetails formDetails) {
        return null;
    }

    @Override
    public void deleteForm(BigDecimal formId) {

    }

    @Override
    public FormDetails getForm(BigDecimal formId) {
        return null;
    }

    @Override
    public FormList getForms() {
        return null;
    }

    @Override
    public FormDetails updateForm(@Valid FormDetails formDetails) {
        return null;
    }

}
