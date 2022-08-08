package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.api.mapper.CodeTableMapper;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.LocationsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Location;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationList;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.logging.Logger;

public class LocationsApiImpl implements LocationsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(LocationsApiImpl.class));

    @Inject
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Inject
    CodeTableMapper codeTableMapper;

    @Override
    @RolesAllowed("data-view")
    public LocationList getLocations() {

        logger.info("Get location request received");

        return codeTableMapper.toLocations("dummyValue", speedmentClientService.getLocation());

    }


}
