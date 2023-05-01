package ca.bc.gov.open.jag.api.util.mapping;

import ca.bc.gov.open.jag.api.util.MappingUtils;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Designation;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class CreateDesignationsTest {

    @Test
    @DisplayName("Success: should return pop designations")
    public void testSuccessCreateDesignations() {


        List<Designation> result = MappingUtils.createDesignations("1","2","3","4");

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals("1", result.get(0).getType());
        Assertions.assertEquals("2", result.get(1).getType());
        Assertions.assertEquals("3", result.get(2).getType());
        Assertions.assertEquals("4", result.get(3).getType());

    }

    @Test
    @DisplayName("Success: should return ucmp designations")
    public void testSuccessCreateUcmpDesignations() {


        List<Designation> result = MappingUtils.createUcmpDesignations("1");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("1", result.get(0).getType());

    }

    @Test
    @DisplayName("Success: no designations should return empty array")
    public void testSuccessCreateDesignationsEmpty() {


        List<Designation> result = MappingUtils.createDesignations("","","","");

        Assertions.assertEquals(0, result.size());

    }

}
