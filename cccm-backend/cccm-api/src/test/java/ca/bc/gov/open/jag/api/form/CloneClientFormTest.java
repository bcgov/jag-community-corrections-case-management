package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CloneForm;
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
import java.util.List;
import java.util.Optional;

@QuarkusTest
public class CloneClientFormTest {
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
    @TestSecurity(user = "userOidc", roles = "form-clone")
    @DisplayName("200: should return new id")
    public void testCloneFormEndpoint() {

        Mockito.when(clientFormSaveService.cloneClientForm(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(BigDecimal.ONE);
        Mockito.when(jwt.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithOverride()));

        CloneForm cloneForm = new CloneForm();
        cloneForm.setClientFormId(BigDecimal.ONE);
        cloneForm.setClientNumber(TEST);

        BigDecimal result = sut.cloneClientForm("123", cloneForm);

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.cloneClientForm("123", null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.cloneClientForm("123", null));

    }

    private JsonObject getRolesWithOverride() {
        JsonReader jsonReader = Json.createReader(new StringReader("{\n" +
                "\t\"roles\": [\n" +
                "\t\t\"form-clone-override\",\n" +
                "\t\t\"blarg\"\n" +
                "\t]\n" +
                "}"));
        return jsonReader.readObject();
    }

}
