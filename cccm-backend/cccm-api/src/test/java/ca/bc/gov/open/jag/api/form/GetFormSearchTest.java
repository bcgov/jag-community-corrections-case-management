package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.Form;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSearchList;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class GetFormSearchTest {

    private static final String TEST_STRING = "TEST";
    private static final LocalDate TEST_DATE = LocalDate.now();

    @Inject
    FormsApiImpl sut;

    @InjectMock
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Test
    @TestSecurity(user = "userOidc", roles = "form-view")
    @DisplayName("200: form search without a type")
    public void testFormSearchEndpoint() throws CCCMException {

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

        List<Form> mockResult = Collections.singletonList(mockForm);

        Mockito.when(speedmentClientService.getFormsByClient(Mockito.any())).thenReturn(mockResult);

        FormSearchList result = sut.getFormSearch(BigDecimal.ONE, true, null);

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
    @TestSecurity(user = "userOidc", roles = "form-view")
    @DisplayName("200: form search with a type")
    public void testFormSearchWithTypeEndpoint() throws CCCMException {

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

        List<Form> mockResult = Collections.singletonList(mockForm);

        Mockito.when(speedmentClientService.getFormsByClient(Mockito.any(), Mockito.any())).thenReturn(mockResult);

        FormSearchList result = sut.getFormSearch(BigDecimal.ONE, true, BigDecimal.ONE);

        Assertions.assertEquals(1, result.getItems().size());

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

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getFormSearch(BigDecimal.ONE, true, null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getFormSearch(BigDecimal.ONE, true, null));

    }

}
