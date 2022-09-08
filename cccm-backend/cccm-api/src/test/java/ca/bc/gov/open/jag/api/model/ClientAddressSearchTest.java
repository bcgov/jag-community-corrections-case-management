package ca.bc.gov.open.jag.api.model;

import ca.bc.gov.open.jag.api.model.service.ClientAddressSearch;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ClientAddressSearchTest {

    private static final String TEST_STRING = "TEST";

    @Test
    @DisplayName("Test Client Address Search Model")
    public void testModel() {

        ClientAddressSearch sut = new ClientAddressSearch(
                TEST_STRING,
                TEST_STRING,
                TEST_STRING,
                TEST_STRING,
                TEST_STRING,
                true
        );

        Assertions.assertEquals(TEST_STRING, sut.getAddressType());
        Assertions.assertEquals(TEST_STRING, sut.getAddress());
        Assertions.assertEquals(TEST_STRING, sut.getCity());
        Assertions.assertEquals(TEST_STRING, sut.getPostalCode());
        Assertions.assertEquals(TEST_STRING, sut.getProvince());
        Assertions.assertTrue(sut.getExpired());

    }

}
