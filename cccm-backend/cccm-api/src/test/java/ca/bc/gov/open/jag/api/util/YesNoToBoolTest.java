package ca.bc.gov.open.jag.api.util;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class YesNoToBoolTest {

    @Test
    @DisplayName("Success: N should be false")
    public void testSuccessIsNIsFalse() {

        Assertions.assertFalse(MappingUtils.yesNoToBool("N"));

    }

    @Test
    @DisplayName("Success: Y should be true")
    public void testSuccessYIsTrue() {

        Assertions.assertTrue(MappingUtils.yesNoToBool("Y"));

    }

    @Test
    @DisplayName("Success: null should return false")
    public void testSuccessNullIsFalse() {
        Assertions.assertFalse(MappingUtils.yesNoToBool(null));
    }

    @Test
    @DisplayName("Success: Garbage should return false")
    public void testSuccessNotYOrNIsFalse() {
        Assertions.assertFalse(MappingUtils.yesNoToBool("GARBAGE"));
    }

}
