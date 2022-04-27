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
public class UpdateClientTest {

    private static final String TEST_VALUE = "TEST";
    private static final LocalDate TEST_DATE = LocalDate.of(1982, 3, 4);

    @Inject
    ClientsApiServiceImpl sut;

    @Test
    @DisplayName("200: should be updated")
    public void testUpdateClientEndpoint() {

        ClientDetails result = sut.updateClient(createTestClient());

        Assertions.assertEquals(BigDecimal.ONE, result.getClientId());
        Assertions.assertEquals(TEST_VALUE, result.getFirstName());
        Assertions.assertEquals(TEST_VALUE, result.getLastName());
        Assertions.assertEquals(TEST_VALUE, result.getGender());
        Assertions.assertEquals(TEST_VALUE, result.getLocation());
        Assertions.assertEquals(TEST_DATE, result.getBirthDate());
        Assertions.assertEquals(TEST_DATE, result.getFinalOrderExpiryDate());

    }

    private ClientDetails createTestClient() {

        ClientDetails clientDetails = new ClientDetails();
        clientDetails.setClientId(BigDecimal.ONE);
        clientDetails.setFirstName(TEST_VALUE);
        clientDetails.setLastName(TEST_VALUE);
        clientDetails.setGender(TEST_VALUE);
        clientDetails.setLocation(TEST_VALUE);
        clientDetails.setBirthDate(TEST_DATE);
        clientDetails.setFinalOrderExpiryDate(TEST_DATE);

        return clientDetails;

    }

}
