package ca.bc.gov.open.jag.api.util.mapping;

import ca.bc.gov.open.jag.api.util.MappingUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@QuarkusTest
public class CalculateAgeTest {

    @Test
    @DisplayName("Success: should return age")
    public void testSuccessCalculateAge() {

        LocalDate testDate = LocalDate.now();
        testDate = testDate.minusYears(5);

        BigDecimal result = MappingUtils.calculateAge(testDate);

        Assertions.assertEquals(BigDecimal.valueOf(5), result);

    }

    @Test
    @DisplayName("Success: null date should return zero")
    public void testSuccessNullCalculateAge() {
        Assertions.assertEquals(BigDecimal.ZERO, MappingUtils.calculateAge(null));
    }

}
