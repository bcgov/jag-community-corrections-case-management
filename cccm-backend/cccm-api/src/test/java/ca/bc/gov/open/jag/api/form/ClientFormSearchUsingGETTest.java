package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@QuarkusTest
public class ClientFormSearchUsingGETTest {

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
    @DisplayName("200: should return answers")
    public void testGetAnswersEndpoint() {

        Mockito.when(clientDataService.clientFormSearch(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(createClientFormSummary());

        List<ClientFormSummary> result = sut.clientFormSearchUsingGET("",true, TEST, TEST, false);

        Assertions.assertEquals(1, result.size());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getClientFormAnswersUsingGET(BigDecimal.ONE,"TEST", null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getClientFormAnswersUsingGET(BigDecimal.ONE,"TEST", null));

    }

    private List<ClientFormSummary> createClientFormSummary() {

        ClientFormSummary clientFormSummary = new ClientFormSummary();
        clientFormSummary.setRatings(new HashMap<>());
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

        return Collections.singletonList(clientFormSummary);

    }
}
