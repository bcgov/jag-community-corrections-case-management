package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientList;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class GetClientsTest {

    @Inject
    ClientsApiServiceImpl sut;

    @Test
    @DisplayName("200: should return clients")
    public void testGetClientsEndpoint() {

        ClientList result = sut.getClients();

        Assertions.assertEquals(1, result.getItems().size());

    }

}
