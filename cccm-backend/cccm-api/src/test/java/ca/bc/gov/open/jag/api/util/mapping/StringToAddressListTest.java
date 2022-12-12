package ca.bc.gov.open.jag.api.util.mapping;

import ca.bc.gov.open.jag.api.util.MappingUtils;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

@QuarkusTest
public class StringToAddressListTest {

    @Test
    @DisplayName("Success: should return address not expired")
    public void testSuccessAddressListExpired() {

        LocalDate testDate = LocalDate.now();
        testDate = testDate.plusDays(2);

        List<Address> result = MappingUtils.stringToAddressList("Test", "TEST", testDate.toString());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("TEST", result.get(0).getType());
        Assertions.assertTrue(result.get(0).getPrimary());
        Assertions.assertFalse(result.get(0).getExpired());
        Assertions.assertEquals("Test", result.get(0).getFullAddress());

    }

    @Test
    @DisplayName("Success: should return address expired")
    public void testSuccessAddressList() {

        LocalDate testDate = LocalDate.now();
        testDate = testDate.minusYears(1);

        List<Address> result = MappingUtils.stringToAddressList("Test", "TEST", testDate.toString());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("TEST", result.get(0).getType());
        Assertions.assertTrue(result.get(0).getPrimary());
        Assertions.assertTrue(result.get(0).getExpired());
        Assertions.assertEquals("Test", result.get(0).getFullAddress());

    }

    @Test
    @DisplayName("Success: no address should return empty array")
    public void testSuccessAddressEmpty() {

        List<Address> result = MappingUtils.stringToAddressList(null, null, null);

        Assertions.assertEquals(0, result.size());

    }

}
