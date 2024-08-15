package ca.bc.gov.open.jag.api.user;

import ca.bc.gov.open.jag.api.model.data.Location;
import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationCode;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationCodeList;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class GetUserLocationsTest {

    private static final String TEST_CD = "CD";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    UserApiImpl sut;

    @InjectMock
    UserDataService userDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "data-view")
    @DisplayName("200: should return locations")
    public void testGetUserLocationsEndpoint() {

        LocationCode location = new LocationCode();
        location.setKey(TEST_CD);
        location.setValue(TEST_VALUE);
        location.setLocationTypeCode(TEST_VALUE);

        LocationCodeList codeListMock = new LocationCodeList();
        codeListMock.addItemsItem(location);

        Mockito.when(userDataService.getLocations(any())).thenReturn(codeListMock);

        LocationCodeList result = sut.getUserLocations();

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_CD, result.getItems().get(0).getKey());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getValue());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getLocationTypeCode());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getLocationsTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getUserLocations());

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getLocationTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getUserLocations());

    }

}
