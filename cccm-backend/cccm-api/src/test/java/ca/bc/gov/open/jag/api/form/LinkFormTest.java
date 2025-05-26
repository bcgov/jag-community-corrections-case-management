package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LinkFormInput;
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

@QuarkusTest
public class LinkFormTest {

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
    @TestSecurity(user = "userOidc", roles = "form-update")
    @DisplayName("201: should link form")
    public void testGetSuccess() {

        Mockito.when(clientDataService.getClientFormAnswersSummary(Mockito.any(), Mockito.any(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(TEST);

        LinkFormInput linkFormInput = new LinkFormInput();
        linkFormInput.setClientFormId(BigDecimal.ONE);
        linkFormInput.setClientNumber(TEST);
        linkFormInput.setFormLevelComments(TEST);
        linkFormInput.setLinkedClientFormId(BigDecimal.ONE);
        linkFormInput.setPlanSummary(TEST);

        Assertions.assertDoesNotThrow(() -> sut.linkForm(linkFormInput, "123"));

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "form-update")
    @DisplayName("400: should return error")
    public void testGetError() {

        Mockito.when(clientDataService.getClientFormAnswersSummary(Mockito.any(), Mockito.any(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(TEST);

        LinkFormInput linkFormInput = new LinkFormInput();
        linkFormInput.setClientFormId(BigDecimal.ONE);
        linkFormInput.setClientNumber(TEST);
        linkFormInput.setFormLevelComments(TEST);
        linkFormInput.setPlanSummary(TEST);

        Assertions.assertThrows(CCCMException.class, () -> sut.linkForm(linkFormInput, "123"));

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.linkForm(new LinkFormInput(), null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.linkForm(new LinkFormInput(), null));

    }

}
