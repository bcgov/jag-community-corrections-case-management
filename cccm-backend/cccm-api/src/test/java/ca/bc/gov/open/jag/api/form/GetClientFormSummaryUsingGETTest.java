package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@QuarkusTest
public class GetClientFormSummaryUsingGETTest {

    private static final String TEST = "TEST";

    @Inject
    FormsApiImpl sut;

    @InjectMock
    ClientDataService clientDataService;

    @InjectMock
    FormDataService formDataService;

    @InjectMock
    ClientFormSaveService clientFormSaveService;

    @InjectMock
    ValidationService validationService;

    @InjectMock
    JsonWebToken jwt;

    @Test
    @TestSecurity(user = "userOidc", roles = "form-view")
    @DisplayName("200: should return summary")
    public void testGetSuccess() {

        Mockito.when(clientDataService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(createClientFormSummary());

        ClientFormSummary result = sut.getClientFormSummaryUsingGET(BigDecimal.ONE, TEST, "123");

        Assertions.assertEquals(TEST, result.getCreatedByIdir());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getClientFormSummaryUsingGET(BigDecimal.ONE, "TEST",null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getClientFormSummaryUsingGET(BigDecimal.ONE, "TEST",null));

    }

    private ClientFormSummary createClientFormSummary() {

        ClientFormSummary clientFormSummary = new ClientFormSummary();
        clientFormSummary.setRatings(new ArrayList<>());
        clientFormSummary.setCreatedByIdir(TEST);
        clientFormSummary.setCreatedBy(TEST);
        clientFormSummary.setCompletedDate(LocalDate.now());
        clientFormSummary.setSupervisionRating(TEST);
        clientFormSummary.setSupervisionLevel(TEST);
        clientFormSummary.setLocked(false);
        clientFormSummary.setReassessment(false);
        clientFormSummary.setComplete(false);
        clientFormSummary.setMostRecent(false);
        clientFormSummary.setRelatedClientFormId(BigDecimal.ONE);
        clientFormSummary.setCreatedDate(LocalDate.now());
        clientFormSummary.setUpdatedDate(LocalDate.now());
        clientFormSummary.setUpdatedBy(TEST);
        clientFormSummary.setLocation(TEST);
        clientFormSummary.setStatus(TEST);
        clientFormSummary.setId(BigDecimal.ONE);
        clientFormSummary.setSummary(TEST);

        return clientFormSummary;

    }

}
