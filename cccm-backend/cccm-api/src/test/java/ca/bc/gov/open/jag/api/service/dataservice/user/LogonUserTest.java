package ca.bc.gov.open.jag.api.service.dataservice.user;

import ca.bc.gov.open.jag.api.Keys;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.Location;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LogonResult;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@QuarkusTest
public class LogonUserTest {

    private static final BigDecimal TEST_ID = BigDecimal.ONE;
    private static final String TEST_CD = "CODE";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    UserDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return location and all locations")
    public void testLogonUserLocationAll() {

        Location locationMock = new Location();
        locationMock.setId(TEST_ID);
        locationMock.setlotyCd(Keys.LOCATION_TYPE_COMMUNITY);
        locationMock.setAlternateCd(TEST_CD);
        locationMock.setDsc(TEST_VALUE);

        Location location2 = new Location();
        location2.setId(TEST_ID);
        location2.setlotyCd("TEST");
        location2.setAlternateCd(TEST_CD);
        location2.setDsc(TEST_VALUE);

        List<Location> locationListMock = Arrays.asList(locationMock, location2);

        Mockito.when(obridgeClientService.getOracleId(Mockito.any())).thenReturn(TEST_ID.toPlainString());
        Mockito.when(obridgeClientService.getLocations(Mockito.any())).thenReturn(locationListMock);
        Mockito.when(obridgeClientService.getLocation(Mockito.any())).thenReturn(locationMock);
        Mockito.doNothing().when(obridgeClientService).setLoginDate(Mockito.any());

        LogonResult result = sut.logonUser("test@idir", Keys.LOCATION_TYPE_ALL);

        Assertions.assertEquals(2, result.getLocations().getItems().size());
        Assertions.assertEquals(TEST_ID.toPlainString(), result.getLocations().getItems().get(0).getKey());
        Assertions.assertEquals(TEST_VALUE, result.getLocations().getItems().get(0).getValue());
        Assertions.assertEquals(TEST_ID.toPlainString(), result.getDefaultLocation().getKey());
        Assertions.assertEquals(TEST_VALUE, result.getDefaultLocation().getValue());

    }

    @Test
    @DisplayName("Success: should return location and community locations")
    public void testLogonUserLocationCommunity() {

        Location locationMock = new Location();
        locationMock.setId(TEST_ID);
        locationMock.setlotyCd(Keys.CCCM_LOCATION);
        locationMock.setAlternateCd(TEST_CD);
        locationMock.setDsc(TEST_VALUE);

        Location location2 = new Location();
        location2.setId(TEST_ID);
        location2.setlotyCd("TEST");
        location2.setAlternateCd(TEST_CD);
        location2.setDsc(TEST_VALUE);

        List<Location> locationListMock = Arrays.asList(locationMock, location2);

        Mockito.when(obridgeClientService.getOracleId(Mockito.any())).thenReturn(TEST_ID.toPlainString());
        Mockito.when(obridgeClientService.getLocations(Mockito.any())).thenReturn(locationListMock);
        Mockito.when(obridgeClientService.getLocation(Mockito.any())).thenReturn(locationMock);
        Mockito.doNothing().when(obridgeClientService).setLoginDate(Mockito.any());

        LogonResult result = sut.logonUser("test@idir", Keys.LOCATION_TYPE_COMMUNITY);

        Assertions.assertEquals(1, result.getLocations().getItems().size());
        Assertions.assertEquals(TEST_ID.toPlainString(), result.getLocations().getItems().get(0).getKey());
        Assertions.assertEquals(TEST_VALUE, result.getLocations().getItems().get(0).getValue());

    }

    @Test
    @DisplayName("Error: location type was not provided")
    public void testLogonUserNoLocation() {

        Location locationMock = new Location();
        locationMock.setId(TEST_ID);
        locationMock.setlotyCd(Keys.LOCATION_TYPE_COMMUNITY);
        locationMock.setAlternateCd(TEST_CD);
        locationMock.setDsc(TEST_VALUE);

        Location location2 = new Location();
        location2.setId(TEST_ID);
        location2.setlotyCd("TEST");
        location2.setAlternateCd(TEST_CD);
        location2.setDsc(TEST_VALUE);

        List<Location> locationListMock = Arrays.asList(locationMock, location2);

        Mockito.when(obridgeClientService.getOracleId(Mockito.any())).thenReturn(TEST_ID.toPlainString());
        Mockito.when(obridgeClientService.getLocations(Mockito.any())).thenReturn(locationListMock);
        Mockito.when(obridgeClientService.getLocation(Mockito.any())).thenReturn(locationMock);
        Mockito.doNothing().when(obridgeClientService).setLoginDate(Mockito.any());

        Assertions.assertThrows(CCCMException.class, () -> sut.logonUser("test@idir", "TEST"));

    }

}
