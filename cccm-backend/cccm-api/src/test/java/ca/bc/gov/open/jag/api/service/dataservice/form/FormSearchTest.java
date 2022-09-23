package ca.bc.gov.open.jag.api.service.dataservice.form;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.Form;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSearchList;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class FormSearchTest {

    private static final String TEST_STRING = "TEST";
    private static final LocalDate TEST_DATE = LocalDate.now();

    @Inject
    FormDataService sut;

    @Test
    @DisplayName("Success: form search without a type")
    public void testFormSearch() throws CCCMException {


        FormSearchList result = sut.formSearch("01", true, null);
        //Temporary removal unitl functionality moved to obridge
        Assertions.assertEquals(0, result.getItems().size());
        /**
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getAssessmentStatus());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getCompletedBy());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getCreatedLocation());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getCrnaRating());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getType());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getSaraRating());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getStatus());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getSupervisionRating());
        Assertions.assertEquals(TEST_DATE.toString(), result.getItems().get(0).getUpdateDate());**/

    }

    @Test
    @DisplayName("Success: form search with a type")
    public void testFormSearchWithType() throws CCCMException {

        FormSearchList result = sut.formSearch("01", true, "01");

        Assertions.assertEquals(0, result.getItems().size());
        //Temporary removal unitl functionality moved to obridge
        /**Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getAssessmentStatus());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getCompletedBy());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getCreatedLocation());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getCrnaRating());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getType());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getSaraRating());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getStatus());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getSupervisionRating());
        Assertions.assertEquals(TEST_DATE.toString(), result.getItems().get(0).getUpdateDate());**/

    }

    private List<Form> createFormList() {

        Form mockForm = new Form();
        mockForm.setAssessmentStatus(TEST_STRING);
        mockForm.setCompletedBy(TEST_STRING);
        mockForm.setCreatedLocation(TEST_STRING);
        mockForm.setCrnaRating(TEST_STRING);
        mockForm.setFormType(TEST_STRING);
        mockForm.setSaraRating(TEST_STRING);
        mockForm.setStatus(TEST_STRING);
        mockForm.setSupervisionRating(TEST_STRING);
        mockForm.setUpdateDate(TEST_DATE);

        return Collections.singletonList(mockForm);

    }

}
