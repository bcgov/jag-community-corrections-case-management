package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.cccm.api.openapi.FormtypesApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormTypeList;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class FormTypesApiServiceImpl implements FormtypesApi {

    @Override
    @Transactional
    public FormTypeList getFormTypes() {
        return null;
    }

}
