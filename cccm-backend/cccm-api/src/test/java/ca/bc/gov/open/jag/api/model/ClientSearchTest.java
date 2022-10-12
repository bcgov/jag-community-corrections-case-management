package ca.bc.gov.open.jag.api.model;

import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@QuarkusTest
public class ClientSearchTest {

    private static final String TEST_STRING = "TEST";

    @Test
    @DisplayName("Test Client Search Model")
    public void testModel() {

        ClientSearch sut = new ClientSearch(TEST_STRING, false, TEST_STRING, 1, 1, 1, true, TEST_STRING, TEST_STRING, TEST_STRING, TEST_STRING, BigDecimal.ONE);

        Assertions.assertEquals(TEST_STRING, sut.getLastName());
        Assertions.assertEquals(false, sut.getSoundex());
        Assertions.assertEquals(TEST_STRING, sut.getGivenName());
        Assertions.assertEquals(1, sut.getBirthYear());
        Assertions.assertEquals(1, sut.getAge());
        Assertions.assertEquals(1, sut.getRange());
        Assertions.assertEquals(TEST_STRING, sut.getIdentifier());
        Assertions.assertEquals(TEST_STRING, sut.getGender());
        Assertions.assertTrue(sut.getLocation());
        Assertions.assertEquals(TEST_STRING, sut.getIdentifierType());
        Assertions.assertEquals(TEST_STRING, sut.getUser());
        Assertions.assertEquals(BigDecimal.ONE, sut.getLocationId());

    }

}
