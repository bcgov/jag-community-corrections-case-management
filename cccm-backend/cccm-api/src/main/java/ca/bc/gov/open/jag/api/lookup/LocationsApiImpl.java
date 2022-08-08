package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.api.service.CodeTableService;
import ca.bc.gov.open.jag.cccm.api.openapi.LocationsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationList;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.logging.Logger;

public class LocationsApiImpl implements LocationsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(LocationsApiImpl.class));

    @Inject
    CodeTableService codeTableService;

    @Override
    @RolesAllowed("data-view")
    public LocationList getLocations() {

        logger.info("Get location request received");

        return codeTableService.getLocationCodeTable();

    }


}
