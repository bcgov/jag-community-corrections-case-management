package ca.bc.gov.open.jag.api.service.dataservice.clientForm;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.Answer;
import ca.bc.gov.open.jag.api.model.data.ClientFormAnswers;
import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.model.service.UpdateForm;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static ca.bc.gov.open.jag.api.Keys.*;

@QuarkusTest
public class CompleteFormTest {

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
    @DisplayName("Success: Form is completed by owner")
    public void testCompleteFormIsOwner() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(CRNA_FORM_TYPE, null, "TEST", null, null));
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(createClientFormAnswers());
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateCRNA(Mockito.any(), Mockito.any())).thenReturn(new ValidationResult());
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, false,"TEST@idir", true, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Success: Form is completed by supervisor")
    public void testCompleteFormIsOwnerHasOverride() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, null, "TESTER", null, null));
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateSARA(Mockito.any())).thenReturn(new ValidationResult());
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, true,"TEST@idir", true, Collections.EMPTY_LIST),"1"));

    }


    @Test
    @DisplayName("Success: Form is completed with SARA child instance")
    public void testCompleteWithSARAChild() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(CRNA_FORM_TYPE, BigDecimal.ONE, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(createClientFormAnswers());
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateCRNA(Mockito.any(), Mockito.any())).thenReturn(new ValidationResult());
        Mockito.when(validationService.validateSARA(Mockito.any())).thenReturn(new ValidationResult());
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setLinkedClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, true,"TEST@idir", true, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Success: Form is completed with child instance")
    public void testCompleteWithChild() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, BigDecimal.ONE, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateCRNA(Mockito.any(), Mockito.any())).thenReturn(new ValidationResult());
        Mockito.when(validationService.validateSARA(Mockito.any())).thenReturn(new ValidationResult());
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setLinkedClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, true,"TEST@idir", true, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Error SARA: Child for with validation error")
    public void testSARAWithChildError() throws IOException {

        ValidationResult validationResult = new ValidationResult();
        validationResult.setErrors(Collections.singletonList(new ValidationError()));

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, BigDecimal.ONE, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateCRNA(Mockito.any(), Mockito.any())).thenReturn(validationResult);
        Mockito.when(validationService.validateSARA(Mockito.any())).thenReturn(new ValidationResult());
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setLinkedClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertThrows(CCCMException.class, () ->  sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, true,"TEST@idir", true, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Error SARA: Parent for with validation error")
    public void testSARAWithValidationError() throws IOException {

        ValidationResult validationResult = new ValidationResult();
        validationResult.setErrors(Collections.singletonList(new ValidationError()));

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, BigDecimal.ONE, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateSARA(Mockito.any())).thenReturn(validationResult);
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setLinkedClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertThrows(CCCMException.class, () ->  sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, true,"TEST@idir", true, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Error CRNA: Child for with validation error")
    public void testCRNAWithChildError() throws IOException {

        ValidationResult validationResult = new ValidationResult();
        validationResult.setErrors(Collections.singletonList(new ValidationError()));

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(CRNA_FORM_TYPE, BigDecimal.ONE, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateSARA(Mockito.any())).thenReturn(validationResult);
        Mockito.when(validationService.validateCRNA(Mockito.any(), Mockito.any())).thenReturn(new ValidationResult());
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setLinkedClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");


        Assertions.assertThrows(CCCMException.class, () ->  sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, true,"TEST@idir", true, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Error CRNA: Parent for with validation error")
    public void testCRNAWithValidationError() throws IOException {

        ValidationResult validationResult = new ValidationResult();
        validationResult.setErrors(Collections.singletonList(new ValidationError()));

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(CRNA_FORM_TYPE, BigDecimal.ONE, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateCRNA(Mockito.any(), Mockito.any())).thenReturn(validationResult);
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setLinkedClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertThrows(CCCMException.class, () ->  sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, true,"TEST@idir", true, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Success: Form is completed by owner ACUTE")
    public void testCompleteFormIsOwnerACUTE() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(ACUTE_FORM_TYPE, null, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateACUTE(Mockito.any())).thenReturn(new ValidationResult());
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, false,"TEST@idir", true, Collections.EMPTY_LIST),"1"));

    }

    @Test
    @DisplayName("Success: Form is completed by owner ACUTE requires new form")
    public void testEditFormIsOwnerACUTERequiresNewForm() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(ACUTE_FORM_TYPE, null, "TEST", "M", null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any())).thenReturn(Collections.singletonList(createClientForm(ACUTE_FORM_TYPE, null, "TEST", "L", LocalDate.now())));
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateACUTE(Mockito.any())).thenReturn(new ValidationResult());
        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(new CodeTable("123", "TEST")));
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput updateFormInput = new UpdateFormInput();
        updateFormInput.setClientFormId(BigDecimal.ONE);
        updateFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(updateFormInput, BigDecimal.ONE, false,"TEST@idir", true, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Error ACUTE: with validation error")
    public void testACUTEWithValidationError() throws IOException {

        ValidationResult validationResult = new ValidationResult();
        validationResult.setErrors(Collections.singletonList(new ValidationError()));

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(ACUTE_FORM_TYPE,null, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateACUTE(Mockito.any())).thenReturn(validationResult);
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setLinkedClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertThrows(CCCMException.class, () ->  sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, true,"TEST", true, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Success: Form is completed by owner STAT99r")
    public void testCompleteFormIsOwnerSTAT99R() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(STATIC99R_FORM_TYPE, null, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateStatic99r(Mockito.any())).thenReturn(new ValidationResult());
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, false,"TEST@idir", true, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Success: Form is completed by owner STAT99R")
    public void testEditFormIsOwnerSTAT99RRequiresNewForm() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(STATIC99R_FORM_TYPE, null, "TEST", "M", null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(validationService.validateStatic99r(Mockito.any())).thenReturn(new ValidationResult());
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(new CodeTable("123", "TEST")));
        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any())).thenReturn(Collections.singletonList(createClientForm(STATIC99R_FORM_TYPE, null, "TEST", "L", LocalDate.now())));
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput updateFormInput = new UpdateFormInput();
        updateFormInput.setClientFormId(BigDecimal.ONE);
        updateFormInput.setClientNumber("TEST");

        Assertions.assertDoesNotThrow(() -> sut.editForm(new UpdateForm(updateFormInput, BigDecimal.ONE, false,"TEST@idir", true, Collections.EMPTY_LIST), "1"));

    }

    @Test
    @DisplayName("Error STABLE: with validation error")
    public void testSTABLEWithValidationError() throws IOException {

        ValidationResult validationResult = new ValidationResult();
        validationResult.setErrors(Collections.singletonList(new ValidationError()));

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(STABLE_FORM_TYPE,null, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateStable(Mockito.any())).thenReturn(validationResult);
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setLinkedClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertThrows(CCCMException.class, () ->  sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, true,"TEST", true, Collections.EMPTY_LIST), "1"));

    }
    @Test
    @DisplayName("Error OVERALL: with validation error")
    public void testOVERALLWithValidationError() throws IOException {

        ValidationResult validationResult = new ValidationResult();
        validationResult.setErrors(Collections.singletonList(new ValidationError()));

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(OVERALL_FORM_TYPE,null, "TEST", null, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn("");
        Mockito.when(validationService.validateSOOverall(Mockito.any())).thenReturn(validationResult);
        Mockito.when(userDataService.getOracleId(Mockito.any())).thenReturn("TEST");

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setLinkedClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertThrows(CCCMException.class, () ->  sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, true,"TEST", true, Collections.EMPTY_LIST),"1"));

    }
    @Test
    @DisplayName("Error: Form Type Invalid")
    public void testDoesNotHaveOverrideorOwner() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, null, "TESTER", null, null));
        Mockito.when(obridgeClientService.getClientFormAnswersObject(Mockito.any(), Mockito.any())).thenReturn(new ClientFormAnswers());

        UpdateFormInput completeFormInput = new UpdateFormInput();
        completeFormInput.setClientFormId(BigDecimal.ONE);
        completeFormInput.setClientNumber("TEST");

        Assertions.assertThrows(CCCMException.class, () -> sut.editForm(new UpdateForm(completeFormInput, BigDecimal.ONE, false,"TEST@idir", true, Collections.EMPTY_LIST),"1"));

    }


    private ClientFormSummary createClientForm(String module, BigDecimal relatedFormId, String createdBy, String superVisionRating, LocalDate completedDate) {

        ClientFormSummary clientFormSummary = new ClientFormSummary();

        clientFormSummary.setFormTypeId(BigDecimal.ONE);
        clientFormSummary.setMostRecent(true);
        clientFormSummary.setModule(module);
        clientFormSummary.setRelatedClientFormId(relatedFormId);
        clientFormSummary.setSupervisionRating(superVisionRating);

        Rating rating = new Rating();
        rating.setFormType("CRNA");
        rating.setText(HIGH);

        clientFormSummary.getRatings().add(rating);
        clientFormSummary.setCompletedDate(completedDate);
        clientFormSummary.setCreatedBy(createdBy);
        clientFormSummary.setCreatedByIdir(createdBy);
        return clientFormSummary;

    }

    private ClientFormAnswers createClientFormAnswers() {
        ClientFormAnswers clientFormAnswers = new ClientFormAnswers();
        Answer answerSuper = new Answer();
        answerSuper.setText("H");
        answerSuper.setSection(1);
        answerSuper.setSequence(1);
        Answer answerRisk = new Answer();
        answerRisk.setText("M");
        answerRisk.setSection(1);
        answerRisk.setSequence(2);
        Answer answerNeeds = new Answer();
        answerNeeds.setText("L");
        answerNeeds.setSection(1);
        answerNeeds.setSequence(3);

        clientFormAnswers.setClientFormId(BigDecimal.ONE);
        clientFormAnswers.setFormComments("TEST");
        clientFormAnswers.setPlanSummary("TEST");
        clientFormAnswers.setSourcesContacted("TEST");
        clientFormAnswers.setAnswers(Arrays.asList(answerSuper, answerRisk, answerNeeds));

        return clientFormAnswers;

    }

}
