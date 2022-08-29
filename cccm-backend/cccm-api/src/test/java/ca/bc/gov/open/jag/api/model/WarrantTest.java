package ca.bc.gov.open.jag.api.model;

import ca.bc.gov.open.jag.api.model.data.Warrant;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class WarrantTest {

    @Test
    @DisplayName("Test Warrant Model")
    public void testWarrantModel() {

        Warrant sut = new Warrant();

        sut.setCharge("TEST");
        sut.setCourtFileNumber("TEST");
        sut.setIssuedDate("TEST");

        Assertions.assertEquals("TEST", sut.getCharge());
        Assertions.assertEquals("TEST", sut.getCourtFileNumber());
        Assertions.assertEquals("TEST", sut.getIssuedDate());

    }

}
