package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.UpdateFormInput;
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
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@QuarkusTest
public class CompleteFormTest {

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
    @DisplayName("200: should complete")
    public void testCompleteEndpoint() {

        Mockito.doNothing().when(clientFormSaveService).editForm(Mockito.any(), Mockito.anyString());
        Mockito.when(jwt.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithOverride()));

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setLinkedClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber(TEST);
        completeFormInput.setClientFormId(BigDecimal.ONE);

        Assertions.assertDoesNotThrow(() -> sut.completeForm(completeFormInput, "123"));

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "form-update")
    @DisplayName("400: throw Bad Request exception")
    public void addTestBadRequest() {

        Assertions.assertThrows(NoSuchElementException.class, () -> sut.completeForm(null, "123"));

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.completeForm(new UpdateFormInput(), TEST));

    }


    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.completeForm(new UpdateFormInput(), TEST));

    }

    private JsonObject getRolesWithOverride() {
        JsonReader jsonReader = Json.createReader(new StringReader("{\n" +
                "\t\"roles\": [\n" +
                "\t\t\"form-override\",\n" +
                "\t\t\"blarg\"\n" +
                "\t]\n" +
                "}"));
        return jsonReader.readObject();
    }

}
