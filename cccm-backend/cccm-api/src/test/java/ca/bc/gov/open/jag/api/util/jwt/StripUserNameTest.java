package ca.bc.gov.open.jag.api.util.jwt;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.util.JwtUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class StripUserNameTest {

    @Test
    @DisplayName("Success: should return string")
    public void testSuccessStripname() {


        String result = JwtUtils.stripUserName("test@idir");

        Assertions.assertEquals("test", result);

    }

    @Test
    @DisplayName("Error: null user should throw")
    public void testErrorNull() {

        Assertions.assertThrows(CCCMException.class, () ->  JwtUtils.stripUserName(null));

    }

    @Test
    @DisplayName("Error: blank user should throw")
    public void testErrorBlank() {

        Assertions.assertThrows(CCCMException.class, () ->  JwtUtils.stripUserName(""));

    }

    @Test
    @DisplayName("Error: missing domain ")
    public void testErrorMissing() {

        Assertions.assertThrows(CCCMException.class, () ->  JwtUtils.stripUserName("test"));

    }

}
