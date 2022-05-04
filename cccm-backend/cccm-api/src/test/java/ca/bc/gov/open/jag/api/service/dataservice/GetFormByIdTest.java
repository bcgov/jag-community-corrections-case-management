package ca.bc.gov.open.jag.api.service.dataservice;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.service.DataServiceImpl;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;

@QuarkusTest
public class GetFormByIdTest {

    private static final LocalDate TEST_DATE = LocalDate.of(2021, 12, 14);

    @Inject
    DataServiceImpl sut;

    @Test
    @DisplayName("Success: should return form data")
    public void getDataTest() throws CCCMException {

        FormDetails result = sut.getFormById(BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result.getFormId());
        Assertions.assertEquals(TEST_DATE, result.getCompletedDate());
        Assertions.assertEquals("Doe, Jane", result.getCreatedBy());
        Assertions.assertEquals(TEST_DATE, result.getCreatedDate());
        Assertions.assertEquals("Initial Assessment", result.getFormType());
        Assertions.assertEquals(TEST_DATE, result.getUpdateDate());
        Assertions.assertEquals(3, result.getFormQuestions().size());

        Assertions.assertEquals(BigDecimal.valueOf(1), result.getFormQuestions().get(0).getQuestionId());
        Assertions.assertEquals("Test Question 1", result.getFormQuestions().get(0).getLabel());
        Assertions.assertEquals("right", result.getFormQuestions().get(0).getOptionsLabelPosition());
        Assertions.assertEquals(false, result.getFormQuestions().get(0).getInline());
        Assertions.assertEquals(false, result.getFormQuestions().get(0).getTableView());
        Assertions.assertEquals("radio1", result.getFormQuestions().get(0).getKey());
        Assertions.assertEquals("radio", result.getFormQuestions().get(0).getType());
        Assertions.assertEquals(true, result.getFormQuestions().get(0).getInput());
        Assertions.assertEquals("testLabel1", result.getFormQuestions().get(0).getDefaultValue());
        Assertions.assertEquals(4, result.getFormQuestions().get(0).getValues().size());

        Assertions.assertEquals(BigDecimal.valueOf(2), result.getFormQuestions().get(1).getQuestionId());
        Assertions.assertEquals("Test Question 2", result.getFormQuestions().get(1).getLabel());

        Assertions.assertEquals(BigDecimal.valueOf(3), result.getFormQuestions().get(2).getQuestionId());
        Assertions.assertEquals("Test Question 3", result.getFormQuestions().get(2).getLabel());

    }
}
