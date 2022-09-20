package ca.bc.gov.open.jag.api.util.mapping;

import ca.bc.gov.open.jag.api.util.MappingUtils;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Warrant;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class StringToAddressListTest {

    @Test
    @DisplayName("Success: should return address")
    public void testSuccessAddressList() {

        List<Address> result = MappingUtils.stringToAddressList("Test");

        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.get(0).getPrimary());
        Assertions.assertFalse(result.get(0).getExpired());
        Assertions.assertEquals("Test", result.get(0).getFullAddress());

    }

    @Test
    @DisplayName("Success: no address should return empty array")
    public void testSuccessAddressEmpty() {

        List<Address> result = MappingUtils.stringToAddressList(null);

        Assertions.assertEquals(0, result.size());

    }

}
