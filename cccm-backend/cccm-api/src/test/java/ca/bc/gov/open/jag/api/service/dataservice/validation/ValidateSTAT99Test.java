package ca.bc.gov.open.jag.api.service.dataservice.validation;

import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.api.service.ValidationServiceImpl;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

@QuarkusTest
public class ValidateSTAT99Test {

    private static final String DATA_ONE = "{\n" +
            "    \"data\": {\n" +
            "        \"S02Q01\": \"A\"\n" +
            "    },\n" +
            "    \"clientFormId\": 389821\n" +
            "}";



    @Inject
    ValidationService sut;

    @Test
    @DisplayName("Success: should validate data")
    public void testValidations() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateStatic99r(DATA_ONE);

        Assertions.assertEquals(11, result.getErrors().size());

    }

    @Test
    @DisplayName("Success: should validate data")
    public void testValidationsEmptyJson() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateStatic99r("{}");

        Assertions.assertEquals(12, result.getErrors().size());

    }

    @Test
    @DisplayName("Success: should validate data")
    public void testValidationsEmpty() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateStatic99r("");

        Assertions.assertEquals(12, result.getErrors().size());

    }


}
