package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationList;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class GetLocationsTest {

    @Inject
    LocationsApiImpl sut;

    @Test
    @DisplayName("200: should return form types")
    public void testGetFormTypesEndpoint() {

        LocationList result = sut.getLocations();

        Assertions.assertEquals(2, result.getItems().size());

    }

}
