package ca.bc.gov.open.jag.api.service.dataservice.clientForm;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.ClientFormAnswers;
import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.model.service.UpdateForm;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static ca.bc.gov.open.jag.api.Keys.*;

@QuarkusTest
public class EditFormTest {

    @Inject
    ClientFormSaveService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    ObjectMapper objectMapper = new ObjectMapper();

    @InjectMock
    UserDataService userDataService;

    @InjectMock
    ValidationService validationService;

    @Test
    @DisplayName("Success: Form is edited by owner")
    public void testEditFormIsOwner() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(CRNA_FORM_TYPE, null, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());

        UpdateFormInput updateFormInput = new UpdateFormInput();
        updateFormInput.setClientFormId(BigDecimal.ONE);
        updateFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(updateFormInput, BigDecimal.ONE, false,"TEST@idir", false, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Success: Form is edited by supervisor")
    public void testEditFormIsOwnerHasOverride() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, null, "TESTER", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());

        UpdateFormInput updateFormInput = new UpdateFormInput();
        updateFormInput.setClientFormId(BigDecimal.ONE);
        updateFormInput.setClientNumber("TEST");


        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(updateFormInput, BigDecimal.ONE, true,"TEST@idir", false, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Success: Form is edited by owner ACUTE")
    public void testEditFormIsOwnerACUTE() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(ACUTE_FORM_TYPE, null, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any())).thenReturn(Collections.emptyList());
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());

        UpdateFormInput updateFormInput = new UpdateFormInput();
        updateFormInput.setClientFormId(BigDecimal.ONE);
        updateFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(updateFormInput, BigDecimal.ONE, false,"TEST@idir", false, Collections.EMPTY_LIST), "1"));

    }



    @Test
    @DisplayName("Success: Form is edited by owner STAT99R")
    public void testEditFormIsOwnerSTAT99R() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(STATIC99R_FORM_TYPE, null, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any())).thenReturn(Collections.emptyList());
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());

        UpdateFormInput updateFormInput = new UpdateFormInput();
        updateFormInput.setClientFormId(BigDecimal.ONE);
        updateFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(updateFormInput, BigDecimal.ONE, false,"TEST@idir", false, Collections.EMPTY_LIST), "1"));

    }



    @Test
    @DisplayName("Success: Form is edited by owner Stable")
    public void testEditFormIsOwnerStable() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(STABLE_FORM_TYPE, null, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());

        UpdateFormInput updateFormInput = new UpdateFormInput();
        updateFormInput.setClientFormId(BigDecimal.ONE);
        updateFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(updateFormInput, BigDecimal.ONE, false,"TEST@idir", false, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Success: Form is edited by owner Overall")
    public void testEditFormIsOwnerOverall() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(OVERALL_FORM_TYPE, null, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());

        UpdateFormInput updateFormInput = new UpdateFormInput();
        updateFormInput.setClientFormId(BigDecimal.ONE);
        updateFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(updateFormInput, BigDecimal.ONE, false,"TEST@idir", false, Collections.EMPTY_LIST), "1"));

    }


    @Test
    @DisplayName("Success: Form is edited with child instance")
    public void testEditWithChild() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, BigDecimal.ONE, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());

        UpdateFormInput updateFormInput = new UpdateFormInput();
        updateFormInput.setClientFormId(BigDecimal.ONE);
        updateFormInput.setLinkedClientFormId(BigDecimal.ONE);
        updateFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(updateFormInput, BigDecimal.ONE, true,"TEST@idir", false, Collections.EMPTY_LIST), "1"));

    }
    
    @Test
    @DisplayName("Error: Edit is invalid")
    public void testDoesNotHaveOverrideorOwner() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, null, "TESTER", null, null));
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());

        UpdateFormInput updateFormInput = new UpdateFormInput();
        updateFormInput.setClientFormId(BigDecimal.ONE);
        updateFormInput.setClientNumber("TEST");

        Assertions.assertThrows(CCCMException.class, () -> sut.editForm(new UpdateForm(updateFormInput, BigDecimal.ONE, false,"TEST@idir", false, Collections.EMPTY_LIST), "1"));

    }


    private ClientFormSummary createClientForm(String module, BigDecimal relatedFormId, String createdBy, String superVisionRating, LocalDate completedDate) {

        ClientFormSummary clientFormSummary = new ClientFormSummary();

        clientFormSummary.setFormTypeId(BigDecimal.ONE);
        clientFormSummary.setMostRecent(true);
        clientFormSummary.setModule(module);
        clientFormSummary.setRelatedClientFormId(relatedFormId);
        clientFormSummary.setSupervisionRating(superVisionRating);
        clientFormSummary.setCompletedDate(completedDate);
        clientFormSummary.setCreatedBy(createdBy);
        clientFormSummary.setCreatedByIdir(createdBy);
        return clientFormSummary;

    }

}
