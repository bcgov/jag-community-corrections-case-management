package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.cccm.api.openapi.LocationsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Location;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationList;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.logging.Logger;

public class LocationsApiImpl implements LocationsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(LocationsApiImpl.class));

    @Override
    @RolesAllowed("data-view")
    public LocationList getLocations() {

        logger.info("Get location request received");

        return createMockLocations();

    }

    private LocationList createMockLocations() {

        LocationList mockLocations = new LocationList();
        mockLocations.getItems().add(createMockLocation("victoria", BigDecimal.ONE, "Victoria"));
        mockLocations.getItems().add(createMockLocation("vancouver", BigDecimal.TEN, "Vancouver"));
        return mockLocations;

    }

    private Location createMockLocation(String cd, BigDecimal id, String desc) {

        Location location = new Location();
        location.setLocationCd(cd);
        location.setLocationId(id);
        location.setLocationDescription(desc);
        return location;

    }

}
