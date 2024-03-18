package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSummary;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class GetFormSummariesTest {


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

        Mockito.when(formDataService.getFormSummaries(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(createFormSummary());

        List<FormSummary> result = sut.getFormSummaries(TEST, true);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(BigDecimal.ONE, result.get(0).getId());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getFormSummaries(TEST, true));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getFormSummaries(TEST, true));

    }

    private List<FormSummary> createFormSummary() {

        FormSummary formSummary = new FormSummary();
        formSummary.setDescription(TEST);
        formSummary.setEffectiveDate(LocalDate.now());
        formSummary.setExpiryDate(LocalDate.now());
        formSummary.setId(BigDecimal.ONE);
        formSummary.setModule(TEST);

        return Collections.singletonList(formSummary);

    }

}
