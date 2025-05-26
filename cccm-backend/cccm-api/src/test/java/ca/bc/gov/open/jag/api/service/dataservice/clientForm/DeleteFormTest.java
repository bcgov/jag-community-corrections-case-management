package ca.bc.gov.open.jag.api.service.dataservice.clientForm;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;
import ca.bc.gov.open.jag.cccm.api.openapi.model.UpdateFormInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static ca.bc.gov.open.jag.api.Keys.CRNA_FORM_TYPE;
import static ca.bc.gov.open.jag.api.Keys.SARA_FORM_TYPE;

@QuarkusTest
public class DeleteFormTest {

    @Inject
    ClientFormSaveService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Success: Form is deleted by owner")
    public void testDeleteFormIsOwner() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(CRNA_FORM_TYPE, null, "TEST"));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        Assertions.assertDoesNotThrow(() -> sut.deleteForm(BigDecimal.ONE,"TEST", BigDecimal.ONE,"TEST@idir",false));

    }

    @Test
    @DisplayName("Success: Form is deleted by supervisor")
    public void testDeleteFormIsOwnerHasOverride() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, null, "TESTER"));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        Assertions.assertDoesNotThrow(() -> sut.deleteForm(BigDecimal.ONE,"TEST", BigDecimal.ONE,"TEST@idir",true));

    }

    @Test
    @DisplayName("Error: Delete is invalid")
    public void testDoesNotHaveOverrideorOwner() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, null, "TESTER"));

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertThrows(CCCMException.class, () -> sut.deleteForm(BigDecimal.ONE,"TEST", BigDecimal.ONE,"TEST@idir",false));

    }


    private ClientFormSummary createClientForm(String module, BigDecimal relatedFormId, String createdBy) {

        ClientFormSummary clientFormSummary = new ClientFormSummary();

        clientFormSummary.setFormTypeId(BigDecimal.ONE);
        clientFormSummary.setMostRecent(true);
        clientFormSummary.setModule(module);
        clientFormSummary.setRelatedClientFormId(relatedFormId);
        clientFormSummary.setCreatedBy(createdBy);
        clientFormSummary.setCreatedByIdir(createdBy);
        return clientFormSummary;

    }


}
