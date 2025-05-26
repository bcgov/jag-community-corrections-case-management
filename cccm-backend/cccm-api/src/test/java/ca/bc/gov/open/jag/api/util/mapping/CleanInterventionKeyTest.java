package ca.bc.gov.open.jag.api.util.mapping;

import ca.bc.gov.open.jag.api.util.MappingUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CleanInterventionKeyTest {

    @Test
    @DisplayName("Success: should return string")
    public void testSuccessExpectedPattern() {

        Assertions.assertEquals("Test", MappingUtils.cleanInterventionKey("Test_123"));

    }
    @Test
    @DisplayName("Success: should return string")
    public void testSuccessUnexpected() {

        Assertions.assertEquals("Test123", MappingUtils.cleanInterventionKey("Test123"));
        Assertions.assertEquals("", MappingUtils.cleanInterventionKey(""));

    }

    @Test
    @DisplayName("Success: should return null")
    public void testSuccessNull() {

        Assertions.assertNull(MappingUtils.cleanInterventionKey(null));

    }



}
