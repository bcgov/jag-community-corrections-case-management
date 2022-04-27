package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientDetails;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class UpdateClientTest {

    @Inject
    ClientsApiServiceImpl sut;

    @Test
    @DisplayName("200: should be updated")
    public void testUpdateClientEndpoint() {

        ClientDetails result = sut.updateClient(new ClientDetails());

        Assertions.assertNull(result);

    }

}
