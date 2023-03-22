package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.api.service.CodeTableService;
import ca.bc.gov.open.jag.cccm.api.openapi.LookupApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class LookupApiImpl implements LookupApi {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(LookupApi.class));

    @Inject
    CodeTableService codeTableService;

    @Override
    @RolesAllowed("data-view")
    public CodeList getAddress(String xLocationId) {

        logger.info("Get Address Types Request");

        return codeTableService.getCodes(CodeTableType.ADDRESS_TYPE);

    }


    @Override
    @RolesAllowed("data-view")
    public CodeList getFormTypes(String xLocationId) {

        logger.info("Get Form Types Request");

        return codeTableService.getCodes(CodeTableType.FORM_TYPE);

    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getGenders(String xLocationId) {

        logger.info("Get Gender Types Request");

        return codeTableService.getCodes(CodeTableType.GENDER_TYPE);

    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getIdentifiers(String xLocationId) {

        logger.info("Get Identifier Request");

        return codeTableService.getCodes(CodeTableType.IDENTIFIER_TYPE);

    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getProvince(String xLocationId) {

        logger.info("Get Province List Request");

        return codeTableService.getCodes(CodeTableType.PROVINCE_TYPE);

    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getCity(String xLocationId) {

        logger.info("Get City List Request");

        return codeTableService.getCodes(CodeTableType.CITY_TYPE);

    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getLocations(String xLocationId) {

        logger.info("Get Location Request");

        return codeTableService.getCodes(CodeTableType.LOCATION_TYPE);

    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getInterventionTypesUsingGET() {

        logger.info("Intervention Types Request");

        return codeTableService.getCodes(CodeTableType.INTERVENTION_TYPE);

    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getResponsivityTypesUsingGET() {

        logger.info("Responsivity Types Request");

        return codeTableService.getCodes(CodeTableType.RESPONSIVITY_TYPE);

    }
}
