package ca.bc.gov.open.jag.api.util.mapping;

import ca.bc.gov.open.jag.api.util.MappingUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@QuarkusTest
public class CalculateLockedTest {

    @Test
    @DisplayName("Success: should return false")
    public void testSuccessCalculateLocked() {

        Assertions.assertFalse(MappingUtils.calculateLocked(LocalDate.now().minusDays(59)));

    }

    @Test
    @DisplayName("Success: should return true")
    public void testSuccessCalculateLockedOver60andOver() {

        Assertions.assertTrue(MappingUtils.calculateLocked(LocalDate.now().minusDays(61)));
        Assertions.assertTrue(MappingUtils.calculateLocked(LocalDate.now().minusDays(60)));

    }


    @Test
    @DisplayName("Success: null date should return false")
    public void testSuccessNullCalculateLocked() {
        Assertions.assertFalse(MappingUtils.calculateLocked(null));
    }

}
