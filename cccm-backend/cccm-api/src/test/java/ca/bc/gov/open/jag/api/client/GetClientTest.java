package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientDetails;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;

@QuarkusTest
public class GetClientTest {

    @Inject
    ClientsApiServiceImpl sut;

    @Test
    @DisplayName("200: should return client")
    public void testGetClientEndpoint() {

        ClientDetails result = sut.getClient(BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result.getClientId());
        Assertions.assertEquals("John", result.getFirstName());
        Assertions.assertEquals("Smith", result.getLastName());
        Assertions.assertEquals("M", result.getGender());
        Assertions.assertEquals("Vancouver, BC", result.getLocation());
        Assertions.assertEquals(LocalDate.of(1982, 3, 4), result.getBirthDate());
        Assertions.assertEquals(LocalDate.of(2021, 10, 2), result.getFinalOrderExpiryDate());

    }

}
