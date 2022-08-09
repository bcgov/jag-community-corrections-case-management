package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.api.service.CodeTableService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Location;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationList;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Collections;

@QuarkusTest
public class GetLocationsTest {

    private static final String TEST_CD = "CD";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    LocationsApiImpl sut;

    @InjectMock
    CodeTableService codeTableService;

    @Test
    @TestSecurity(user = "userOidc", roles = "data-view")
    @DisplayName("200: should return form types")
    public void testGetFormTypesEndpoint() {

        LocationList locationList = new LocationList();

        Location location = new Location();
        location.setLocationCd(TEST_CD);
        location.setLocationDescription(TEST_VALUE);
        locationList.setItems(Collections.singletonList(location));


        Mockito.when(codeTableService.locationCodes()).thenReturn(locationList);

        LocationList result = sut.getLocations();

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_CD, result.getItems().get(0).getLocationCd());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getLocationDescription());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getLocationsTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getLocations());

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getLocationsTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getLocations());

    }

}
