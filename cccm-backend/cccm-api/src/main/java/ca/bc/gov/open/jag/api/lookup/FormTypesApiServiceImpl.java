package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.cccm.api.openapi.FormtypesApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormTypeList;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FormTypesApiServiceImpl implements FormtypesApi {

    @Override
    public FormTypeList getFormTypes() {
        return null;
    }

}
