package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Form;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSearchList;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class GetFormSearchTest {

    private static final String TEST_STRING = "TEST";
    private static final LocalDate TEST_DATE = LocalDate.now();

    @Inject
    FormsApiImpl sut;

    @InjectMock
    FormDataService formDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "form-view")
    @DisplayName("200: form search without a type")
    public void testFormSearchEndpoint() throws CCCMException {

        Mockito.when(formDataService.formSearch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createFormList());

        FormSearchList result = sut.getClientForms("01", true, null);

        assertEquals(1, result.getItems().size());
        assertEquals(TEST_STRING, result.getItems().get(0).getAssessmentStatus());
        assertEquals(TEST_STRING, result.getItems().get(0).getCompletedBy());
        assertEquals(TEST_STRING, result.getItems().get(0).getCreatedLocation());
        assertEquals(TEST_STRING, result.getItems().get(0).getCrnaRating());
        assertEquals(TEST_STRING, result.getItems().get(0).getType());
        assertEquals(TEST_STRING, result.getItems().get(0).getSaraRating());
        assertEquals(TEST_STRING, result.getItems().get(0).getStatus());
        assertEquals(TEST_STRING, result.getItems().get(0).getSupervisionRating());
        assertEquals(TEST_DATE.toString(), result.getItems().get(0).getUpdateDate());

    }


    @Test
    @TestSecurity(user = "userOidc", roles = "form-view")
    @DisplayName("200: get form by id")
    public  void getFormById() {
        Mockito.when(formDataService.getForm(BigDecimal.ONE, false )).thenReturn(createSampleFormModel());
        FormDetails result = sut.getFormById(BigDecimal.ONE, false);
        assertNotNull(result);
        assertEquals(result.getFormId(), BigDecimal.ONE);
    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getTestExceptionBadRole() {

        assertThrows(ForbiddenException.class, () -> sut.getClientForms("01", true, null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getTestExceptionNoToken() {

        assertThrows(UnauthorizedException.class, () -> sut.getClientForms("01", true, null));

    }

    private FormDetails createSampleFormModel() {
        FormDetails mockModel = new FormDetails();

        mockModel.setCreatedDate(LocalDate.now().minus(30, ChronoUnit.DAYS));
        mockModel.setFormId(BigDecimal.ONE);
        mockModel.setFormType("CRNA");
        return mockModel;
    }

    private FormSearchList createFormList() {

        FormSearchList formSearchList = new FormSearchList();

        Form mockForm = new Form();
        mockForm.setAssessmentStatus(TEST_STRING);
        mockForm.setCompletedBy(TEST_STRING);
        mockForm.setCreatedLocation(TEST_STRING);
        mockForm.setCrnaRating(TEST_STRING);
        mockForm.setType(TEST_STRING);
        mockForm.setSaraRating(TEST_STRING);
        mockForm.setStatus(TEST_STRING);
        mockForm.setSupervisionRating(TEST_STRING);
        mockForm.setUpdateDate(TEST_DATE.toString());

        formSearchList.setItems(Collections.singletonList(mockForm));

        return formSearchList;

    }

}
