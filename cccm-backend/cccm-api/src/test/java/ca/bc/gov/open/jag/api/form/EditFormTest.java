package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.UpdateFormInput;
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
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Optional;

@QuarkusTest
public class EditFormTest {

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
    @DisplayName("200: should not throw")
    public void testSuccess() {

        Mockito.doNothing().when(clientFormSaveService).editForm(Mockito.any(), Mockito.any());
        Mockito.when(jwt.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithOverride()));

        UpdateFormInput updateFormInput = new UpdateFormInput();

        updateFormInput.setClientFormId(BigDecimal.ONE);
        updateFormInput.setClientNumber(TEST);
        updateFormInput.setLinkedClientFormId(BigDecimal.ONE);

        Assertions.assertDoesNotThrow(() ->  sut.editForm(updateFormInput, "123"));

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.editForm(new UpdateFormInput(),null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.editForm(new UpdateFormInput(),null));

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
