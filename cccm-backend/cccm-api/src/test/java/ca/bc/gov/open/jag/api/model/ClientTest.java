package ca.bc.gov.open.jag.api.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@QuarkusTest
public class ClientTest {

    @Test
    @DisplayName("Test Client Model")
    public void testClientModel() {

        Client sut = new Client();

        sut.setClientNo("01");
        sut.setClientName("TEST");
        sut.setCurrentNameYn("TEST");
        sut.setGenderCode("TEST");
        sut.setBirthDate("TEST");
        sut.setCustodyLocation("TEST");
        sut.setCommunityLocation("TEST");

        Assertions.assertEquals("01", sut.getClientNo());
        Assertions.assertEquals("TEST", sut.getClientName());
        Assertions.assertEquals("TEST", sut.getCurrentNameYn());
        Assertions.assertEquals("TEST", sut.getGenderCode());
        Assertions.assertEquals("TEST", sut.getBirthDate());
        Assertions.assertEquals("TEST", sut.getCustodyLocation());
        Assertions.assertEquals("TEST", sut.getCommunityLocation());

    }

}
