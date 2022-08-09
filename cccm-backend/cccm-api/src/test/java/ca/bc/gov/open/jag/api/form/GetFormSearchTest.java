package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Form;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSearchList;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Collections;

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

        FormSearchList result = sut.getFormSearch("01", true, null);

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getAssessmentStatus());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getCompletedBy());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getCreatedLocation());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getCrnaRating());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getType());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getSaraRating());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getStatus());
        Assertions.assertEquals(TEST_STRING, result.getItems().get(0).getSupervisionRating());
        Assertions.assertEquals(TEST_DATE.toString(), result.getItems().get(0).getUpdateDate());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getFormSearch("01", true, null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getFormSearch("01", true, null));

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
