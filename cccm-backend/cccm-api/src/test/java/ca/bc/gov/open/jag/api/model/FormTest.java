package ca.bc.gov.open.jag.api.model;

import ca.bc.gov.open.jag.api.model.data.Form;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@QuarkusTest
public class FormTest {

    private static final String TEST_STRING = "TEST";
    private static final LocalDate TEST_DATE = LocalDate.now();

    @Test
    @DisplayName("Test Form Model")
    public void testClientModel() {

        Form sut = new Form();

        sut.setAssessmentStatus(TEST_STRING);
        sut.setCompletedBy(TEST_STRING);
        sut.setCreatedLocation(TEST_STRING);
        sut.setCrnaRating(TEST_STRING);
        sut.setFormType(TEST_STRING);
        sut.setSaraRating(TEST_STRING);
        sut.setStatus(TEST_STRING);
        sut.setSupervisionRating(TEST_STRING);
        sut.setUpdateDate(TEST_DATE);


        Assertions.assertEquals(TEST_STRING, sut.getAssessmentStatus());
        Assertions.assertEquals(TEST_STRING, sut.getCompletedBy());
        Assertions.assertEquals(TEST_STRING, sut.getCreatedLocation());
        Assertions.assertEquals(TEST_STRING, sut.getCrnaRating());
        Assertions.assertEquals(TEST_STRING, sut.getFormType());
        Assertions.assertEquals(TEST_STRING, sut.getSaraRating());
        Assertions.assertEquals(TEST_STRING, sut.getStatus());
        Assertions.assertEquals(TEST_STRING, sut.getSupervisionRating());
        Assertions.assertEquals(TEST_DATE, sut.getUpdateDate());
    }

}
