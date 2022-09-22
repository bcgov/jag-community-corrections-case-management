package ca.bc.gov.open.jag.api.util.mapping;

import ca.bc.gov.open.jag.api.util.MappingUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

@QuarkusTest
public class IsExpiredTest {

    @Test
    @DisplayName("Success: date is not expired should be false")
    public void testSuccessIsNotExpired() {

        LocalDate testDate = LocalDate.now();
        testDate = testDate.minusYears(1);

        Boolean result = MappingUtils.isExpired(testDate.toString());

        Assertions.assertFalse(result);

    }

    @Test
    @DisplayName("Success: date is expired should be true")
    public void testSuccessIsExpired() {
        LocalDate testDate = LocalDate.now();
        testDate = testDate.plusDays(2);

        Boolean result = MappingUtils.isExpired(testDate.toString());

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Success: null date should return false")
    public void testSuccessNullIsNotExpired() {
        Assertions.assertFalse(MappingUtils.isExpired(null));
    }

}
