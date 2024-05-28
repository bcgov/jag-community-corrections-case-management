package ca.bc.gov.open.jag.api.service.dataservice.validation;

import ca.bc.gov.open.jag.api.model.data.ClientDates;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.api.service.ValidationServiceImpl;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;

@QuarkusTest
public class ValidateCMRPTest {

    private static final String DATA_ONE = "{\n" +
            "    \"data\": {\n" +
            "        \"S03Q01\": \"A\"\n" +
            "    },\n" +
            "    \"clientFormId\": 389821\n" +
            "}";



    @Inject
    ValidationService sut;

    @Test
    @DisplayName("Success: should validate data")
    public void testValidations() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateCMRP(DATA_ONE, createDates(LocalDate.now().minusMonths(5).toString(), LocalDate.now().minusDays(36).toString()));

        Assertions.assertEquals(0, result.getErrors().size());

    }

    @Test
    @DisplayName("Success: should validate data")
    public void testValidationsEmptyJson() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateCMRP("{}", createDates(LocalDate.now().minusMonths(8).toString(), LocalDate.now().plusDays(30).toString()));

        Assertions.assertEquals(3, result.getErrors().size());

    }

    @Test
    @DisplayName("Success: should validate data")
    public void testValidationsEmpty() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateCMRP("", createDates(LocalDate.now().toString(), LocalDate.now().plusDays(1).toString()));

        Assertions.assertEquals(2, result.getErrors().size());

    }

    @Test
    @DisplayName("Success: should validate data")
    public void testValidationsNullDates() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateCMRP("", createDates(null, null));

        Assertions.assertEquals(1, result.getErrors().size());

    }

    @Test
    @DisplayName("Success: should validate data")
    public void testValidationsEmptyDates() throws IOException {

        sut = new ValidationServiceImpl(new ObjectMapper());

        ValidationResult result = sut.validateCMRP("", createDates("", ""));

        Assertions.assertEquals(1, result.getErrors().size());

    }

    private ClientDates createDates(String crnaDate, String pddDate) {

        ClientDates clientDates = new ClientDates();
        clientDates.setPddDate(pddDate);
        clientDates.setCrnaCompleteDate(crnaDate);

        return clientDates;

    }

}
