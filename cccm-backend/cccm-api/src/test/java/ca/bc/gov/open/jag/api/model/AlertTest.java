package ca.bc.gov.open.jag.api.model;

import ca.bc.gov.open.jag.api.model.data.Alert;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

@QuarkusTest
public class AlertTest {

    @Test
    @DisplayName("Test Alert Model")
    public void testModel() {

        LocalDate testDate = LocalDate.now();

        Alert sut = new Alert();
        sut.setDescription("TEST");

        Assertions.assertEquals("TEST", sut.getDescription());

    }

}
