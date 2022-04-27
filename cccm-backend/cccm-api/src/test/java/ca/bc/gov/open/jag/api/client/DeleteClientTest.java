package ca.bc.gov.open.jag.api.client;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;

@QuarkusTest
public class DeleteClientTest {

    @Inject
    ClientsApiServiceImpl sut;

    @Test
    @DisplayName("200: no error")
    public void testDeleteEndpoint() {

        Assertions.assertDoesNotThrow(() -> sut.deleteClient(BigDecimal.ONE));

    }

}
