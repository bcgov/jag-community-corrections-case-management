package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.api.service.CodeTableService;
import ca.bc.gov.open.jag.cccm.api.openapi.LookupApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class LookupApiImpl implements LookupApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(LookupApiImpl.class));

    @Inject
    CodeTableService codeTableService;

    @Override
    @RolesAllowed("data-view")
    public CodeList getAddress(String xLocationId) {

        logger.info("Get address types request received");

        return codeTableService.getCodes(CodeTableType.ADDRESS_TYPE);

    }


    @Override
    @RolesAllowed("data-view")
    public CodeList getFormTypes(String xLocationId) {

        logger.info("Get form types request received");

        return codeTableService.getCodes(CodeTableType.FORM_TYPE);

    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getGenders(String xLocationId) {

        logger.info("Get gender types request received");

        return codeTableService.getCodes(CodeTableType.GENDER_TYPE);

    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getIdentifiers(String xLocationId) {

        logger.info("Get identifier request received");

        return codeTableService.getCodes(CodeTableType.IDENTIFIER_TYPE);

    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getLocations(String xLocationId) {

        logger.info("Get location request received");

        return codeTableService.getCodes(CodeTableType.LOCATION_TYPE);

    }

    @Override
//    @RolesAllowed("data-view")
    public CodeList getInterventionTypesUsingGET() {
        return codeTableService.getCodes(CodeTableType.INTERVENTION_TYPE);
    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getResponsivityTypesUsingGET() {
        return codeTableService.getCodes(CodeTableType.RESPONSIVITY_TYPE);
    }
}
