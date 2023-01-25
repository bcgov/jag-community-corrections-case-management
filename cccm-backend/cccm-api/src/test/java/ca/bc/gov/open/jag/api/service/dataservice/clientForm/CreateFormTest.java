package ca.bc.gov.open.jag.api.service.dataservice.clientForm;

import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CreateFormInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;


@QuarkusTest
public class CreateFormTest {

    @Inject
    ClientFormSaveService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    ObjectMapper objectMapper = new ObjectMapper();

    @InjectMock
    ValidationService validationService;

    @InjectMock
    UserDataService userDataService;

    @Test
    @DisplayName("Success: CRNA Form is created")
    public void testCreateCRNA() throws IOException {

        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(new CodeTable("123", "TEST")));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        BigDecimal result = sut.createCRNA(createFormInput(null, "TEST"), BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Success: SARA Form is created")
    public void testCreateSARA() throws IOException {

        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(new CodeTable("123", "TEST")));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        BigDecimal result = sut.createSARA(createFormInput(null, "TEST"), BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Success: SARA Form is created")
    public void testCreateSARAWithChild() throws IOException {

        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(new CodeTable("123", "TEST")));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        BigDecimal result = sut.createSARA(createFormInput(BigDecimal.ONE, "TEST"), BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Success: ACUTE Form is created")
    public void testCreateACUTE() throws IOException {

        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(new CodeTable("123", "TEST")));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        BigDecimal result = sut.createACUTE(createFormInput(null, "TEST"), BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Success: STATIC99R Form is created")
    public void testCreateSTATIC99R() throws IOException {

        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(new CodeTable("123", "TEST")));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        BigDecimal result = sut.createStatic99r(createFormInput(null, "TEST"), BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Success: Stable Form is created")
    public void testCreateSTABLE() throws IOException {

        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(new CodeTable("123", "TEST")));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        BigDecimal result = sut.createStable(createFormInput(null, "TEST"), BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Success: Overall Form is created")
    public void testCreateOverall() throws IOException {

        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(new CodeTable("123", "TEST")));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        BigDecimal result = sut.createSOOverall(createFormInput(null, "TEST"), BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    private CreateFormInput createFormInput(BigDecimal linkedFormId, String clientNumber) {
        CreateFormInput createFormInput = new CreateFormInput();
        createFormInput.setLinkedClientFormId(linkedFormId);
        createFormInput.setClientNumber(clientNumber);
        return createFormInput;
    }

}
