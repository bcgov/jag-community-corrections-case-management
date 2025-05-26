package ca.bc.gov.open.jag.api.service.dataservice.user;

import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

@QuarkusTest
public class GetOracleIdTest {

    private static final BigDecimal TEST_ID = BigDecimal.ONE;


    @Inject
    UserDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return ID")
    public void testOracleId() {

        Mockito.when(obridgeClientService.getOracleId(Mockito.any())).thenReturn(TEST_ID.toPlainString());

        String result = sut.getOracleId("test@idir");

        Assertions.assertEquals(TEST_ID.toPlainString(), result);

    }

}
