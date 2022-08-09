package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.api.service.CodeTableService;
import ca.bc.gov.open.jag.cccm.api.openapi.FormtypesApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormTypeList;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.logging.Logger;

@ApplicationScoped
public class FormTypesApiImpl implements FormtypesApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(FormTypesApiImpl.class));

    @Inject
    CodeTableService codeTableService;

    @Override
    @Transactional
    @RolesAllowed("data-view")
    public FormTypeList getFormTypes() {

        logger.info("Get form types request received");

        return codeTableService.formTypeCodes();

    }

}
