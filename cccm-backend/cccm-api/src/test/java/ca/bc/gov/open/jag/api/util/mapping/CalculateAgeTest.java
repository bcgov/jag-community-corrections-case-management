package ca.bc.gov.open.jag.api.util.mapping;

import ca.bc.gov.open.jag.api.util.MappingUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

@QuarkusTest
public class CalculateAgeTest {

    @Test
    @DisplayName("Success: should return age")
    public void testSuccessCalculateAge() {

        LocalDate testDate = LocalDate.now();
        testDate = testDate.minusYears(5);

        BigDecimal result = MappingUtils.calculateAge(testDate.toString());

        Assertions.assertEquals(BigDecimal.valueOf(5), result);

    }

    @Test
    @DisplayName("Success: should return age")
    public void testSuccessCalculateAgeAlternateDate() {

        BigDecimal result = MappingUtils.calculateAge("APRIL     19, 1982");

        Assertions.assertTrue((result.intValue() >= 40));

    }


    @Test
    @DisplayName("Success: null date should return zero")
    public void testSuccessNullCalculateAge() {
        Assertions.assertEquals(BigDecimal.ZERO, MappingUtils.calculateAge(null));
    }

    @Test
    @DisplayName("Error: invalid date should throw exception")
    public void testErrorCalculateAge() {
        Assertions.assertEquals(BigDecimal.ZERO, MappingUtils.calculateAge("GARBAGE"));
    }

}
