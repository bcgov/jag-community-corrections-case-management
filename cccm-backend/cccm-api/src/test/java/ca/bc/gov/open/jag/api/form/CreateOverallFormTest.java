package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CreateFormInput;
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

@QuarkusTest
public class CreateOverallFormTest {

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
    @TestSecurity(user = "userOidc", roles = "form-add")
    @DisplayName("200: should return answers")
    public void testSuccess() {

        Mockito.when(clientFormSaveService.createSOOverall(Mockito.any(), Mockito.any())).thenReturn(BigDecimal.ONE);

        CreateFormInput createFormInput = new CreateFormInput();
        createFormInput.setClientNumber(TEST);
        createFormInput.setLinkedClientFormId(BigDecimal.ONE);

        BigDecimal result = sut.createOverallForm(createFormInput, "123");

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.createOverallForm(new CreateFormInput(),"TEST"));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.createOverallForm(new CreateFormInput(),"TEST"));

    }

}
