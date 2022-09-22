package ca.bc.gov.open.jag.api.service.dataservice.user;

import ca.bc.gov.open.jag.api.model.data.Location;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;

@QuarkusTest
public class GetDefaultLocationTest {

    private static final BigDecimal TEST_ID = BigDecimal.ONE;
    private static final String TEST_CD = "CODE";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    UserDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return form types")
    public void testGetFormTypes() {

        Location locationMock = new Location();
        locationMock.setId(TEST_ID);
        locationMock.setAlternateCd(TEST_CD);
        locationMock.setDsc(TEST_VALUE);
        Mockito.when(obridgeClientService.getOracleId(Mockito.any())).thenReturn(TEST_ID.toPlainString());
        Mockito.when(obridgeClientService.getLocation(Mockito.any())).thenReturn(locationMock);

        Code result = sut.getDefaultLocation("test@idir");

        Assertions.assertEquals(TEST_ID.toPlainString(), result.getKey());
        Assertions.assertEquals(TEST_VALUE, result.getValue());

    }

}
