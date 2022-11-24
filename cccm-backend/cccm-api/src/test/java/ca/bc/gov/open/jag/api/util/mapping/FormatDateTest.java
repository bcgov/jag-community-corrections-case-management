package ca.bc.gov.open.jag.api.util.mapping;

import ca.bc.gov.open.jag.api.util.MappingUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FormatDateTest {

    @Test
    @DisplayName("Success: should return date formatted")
    public void testSuccessCalculateAge() {

        String result = MappingUtils.formatDate("1975-02-24");

        Assertions.assertEquals("1975-02-24", result);

    }

    @Test
    @DisplayName("Success: should return date formatted")
    public void testSuccessCalculateAgeAlternateDate() {

        String result = MappingUtils.formatDate("APRIL     19, 1982");

        Assertions.assertEquals("1982-04-19", result);

    }


    @Test
    @DisplayName("Success: null date should return empty string")
    public void testSuccessNullCalculateAge() {
        Assertions.assertEquals("", MappingUtils.formatDate(null));
    }

    @Test
    @DisplayName("Error: invalid date should throw exception and return empty string")
    public void testErrorCalculateAge() {
        Assertions.assertEquals("", MappingUtils.formatDate("GARBAGE"));
    }



}
