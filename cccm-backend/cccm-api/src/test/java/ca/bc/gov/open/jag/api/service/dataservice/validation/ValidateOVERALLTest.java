package ca.bc.gov.open.jag.api.service.dataservice.validation;

import ca.bc.gov.open.jag.api.model.data.Intervention;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.api.service.ValidationServiceImpl;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@QuarkusTest
public class ValidateOVERALLTest {

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

        ValidationResult result = sut.validateSOOverall(DATA_ONE, new ArrayList<>(), true);

        Assertions.assertEquals(6, result.getErrors().size());

    }

    @Test
    @DisplayName("Success: should validate data")
    public void testValidationsWithCasePlan() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateSOOverall(DATA_ONE, List.of(new Intervention()), false);

        Assertions.assertEquals(7, result.getErrors().size());

    }

    @Test
    @DisplayName("Success: should validate data")
    public void testValidationsEmptyJson() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateSOOverall("{}", new ArrayList<>(), true);

        Assertions.assertEquals(7, result.getErrors().size());

    }

    @Test
    @DisplayName("Success: should validate data")
    public void testValidationsEmpty() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateSOOverall("", new ArrayList<>(), true);

        Assertions.assertEquals(7, result.getErrors().size());

    }

}
