package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientDetails;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;

@QuarkusTest
public class GetClientTest {

    @Inject
    ClientsApiServiceImpl sut;

    @Test
    @DisplayName("200: should return client")
    public void testGetClientEndpoint() {

        ClientDetails result = sut.getClient(BigDecimal.ONE);

        Assertions.assertNull(result);

    }

}
