package ca.bc.gov.open.jag.api.service.dataservice.clientForm;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.service.UpdateForm;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;
import ca.bc.gov.open.jag.cccm.api.openapi.model.UpdateFormInput;
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

import static ca.bc.gov.open.jag.api.Keys.CRNA_FORM_TYPE;
import static ca.bc.gov.open.jag.api.Keys.SARA_FORM_TYPE;

@QuarkusTest
public class CompleteFormTest {

    @Inject
    ClientFormSaveService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Success: Form is completed by owner")
    public void testCompleteFormIsOwner() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any())).thenReturn(createClientForm(CRNA_FORM_TYPE, null, "TEST"));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");
        completeFormInput.setFormLevelComments("TEST");
        completeFormInput.setPlanSummary("TEST");
        completeFormInput.setSourcesContacted("TEST");

        BigDecimal result = sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, false,"TEST@idir", true));

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Success: Form is completed by supervisor")
    public void testCompleteFormIsOwnerHasOverride() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, null, "TESTER"));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");
        completeFormInput.setFormLevelComments("TEST");
        completeFormInput.setPlanSummary("TEST");
        completeFormInput.setSourcesContacted("TEST");

        BigDecimal result = sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, true,"TEST@idir", true));

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Success: Form is completed with child instance")
    public void testCompleteWithChild() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, BigDecimal.ONE, "TEST"));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setLinkedClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");
        completeFormInput.setFormLevelComments("TEST");
        completeFormInput.setPlanSummary("TEST");
        completeFormInput.setSourcesContacted("TEST");

        BigDecimal result = sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, true,"TEST@idir", true));

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Error: Form Type Invalid")
    public void testDoesNotHaveOverrideorOwner() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, null, "TESTER"));

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertThrows(CCCMException.class, () -> sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, false,"TEST@idir", true)));

    }


    private ClientFormSummary createClientForm(String module, BigDecimal relatedFormId, String createdBy) {

        ClientFormSummary clientFormSummary = new ClientFormSummary();

        clientFormSummary.setFormTypeId(BigDecimal.ONE);
        clientFormSummary.setMostRecent(true);
        clientFormSummary.setModule(module);
        clientFormSummary.setRelatedClientFormId(relatedFormId);
        clientFormSummary.setCreatedBy(createdBy);
        return clientFormSummary;

    }

}
