package ca.bc.gov.open.jag.api.service.dataservice.user;

import ca.bc.gov.open.jag.api.model.data.Location;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class GetLocationsTest {

    private static final BigDecimal TEST_ID = BigDecimal.ONE;
    private static final String TEST_CD = "CODE";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    UserDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;



    @Test
    @DisplayName("Success: should return location")
    public void testGetLocations() {

        Location locationMock = new Location();
        locationMock.setId(TEST_ID);
        locationMock.setAlternateCd(TEST_CD);
        locationMock.setDsc(TEST_VALUE);

        List<Location> locationListMock = Collections.singletonList(locationMock);

        Mockito.when(obridgeClientService.getOracleId(Mockito.any())).thenReturn(TEST_ID.toPlainString());
        Mockito.when(obridgeClientService.getLocations(Mockito.any())).thenReturn(locationListMock);

        CodeList result = sut.getLocations("test@idir");

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_ID.toPlainString(), result.getItems().get(0).getKey());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getValue());

    }


}
