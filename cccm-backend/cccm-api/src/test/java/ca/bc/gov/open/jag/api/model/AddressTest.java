package ca.bc.gov.open.jag.api.model;

import ca.bc.gov.open.jag.api.model.data.Address;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@QuarkusTest
public class AddressTest {

    private static final String TEST = "TEST";

    @Test
    @DisplayName("Test Address Model")
    public void testAddressModel() {

        LocalDate testDate = LocalDate.now();

        Address sut = new Address();

        sut.setExpiryDate(TEST);
        sut.setFullAddress(TEST);

        Assertions.assertEquals(TEST, sut.getExpiryDate());
        Assertions.assertEquals(TEST, sut.getFullAddress());

    }

}
