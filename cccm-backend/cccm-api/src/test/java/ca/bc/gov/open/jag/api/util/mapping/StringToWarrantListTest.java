package ca.bc.gov.open.jag.api.util.mapping;

import ca.bc.gov.open.jag.api.util.MappingUtils;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Warrant;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class StringToWarrantListTest {

    @Test
    @DisplayName("Success: should return warrants")
    public void testSuccessWarrantsList() {

        List<Warrant> result = MappingUtils.stringToWarrantList("1,2,3,4");

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals("1", result.get(0).getType());
        Assertions.assertEquals("2", result.get(1).getType());
        Assertions.assertEquals("3", result.get(2).getType());
        Assertions.assertEquals("4", result.get(3).getType());

    }

    @Test
    @DisplayName("Success: no warrants should return empty array")
    public void testSuccessWarrantsEmpty() {

        List<Warrant> result = MappingUtils.stringToWarrantList("");

        Assertions.assertEquals(0, result.size());

    }

}
