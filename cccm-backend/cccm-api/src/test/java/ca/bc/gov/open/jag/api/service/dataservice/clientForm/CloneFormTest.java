package ca.bc.gov.open.jag.api.service.dataservice.clientForm;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.CloneFormRequest;
import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;
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

import static ca.bc.gov.open.jag.api.Keys.*;

@QuarkusTest
public class CloneFormTest {

    private static final String DATA_ONE = "{\n" +
            "    \"data\": {\n" +
            "        \"S02Q01\": \"A\",\n" +
            "        \"S02Q01_COMMENT\": \"update my comments please aasdfsdf\",\n" +
            "        \"S02Q01_intervention_checkbox\": true,\n" +
            "        \"S02Q02\": \"A\",\n" +
            "        \"S02Q02_COMMENT\": \"<ul><li>comments for living arrangements, updated on Oct 20</li><li>sdflsdaf</li><li>sfa</li></ul>\",\n" +
            "        \"S02Q02_intervention_checkbox\": false,\n" +
            "        \"S02Q03\": \"C\",\n" +
            "        \"S02Q03_COMMENT\": \"\",\n" +
            "        \"S02Q03_intervention_checkbox\": false,\n" +
            "        \"S02Q04\": \"D\",\n" +
            "        \"S02Q04_COMMENT\": \"\",\n" +
            "        \"S02Q04_intervention_checkbox\": false,\n" +
            "        \"S02Q05\": \"\",\n" +
            "        \"S02Q05_COMMENT\": \"\",\n" +
            "        \"S02Q05_intervention_checkbox\": false,\n" +
            "        \"S02Q06\": \"D\",\n" +
            "        \"S02Q06_COMMENT\": \"\",\n" +
            "        \"S02Q06_intervention_checkbox\": false,\n" +
            "        \"S02Q07\": \"B\",\n" +
            "        \"S02Q07_COMMENT\": \"\",\n" +
            "        \"S02Q07_intervention_checkbox\": false,\n" +
            "        \"S02Q08\": \"C\",\n" +
            "        \"S02Q08_COMMENT\": \"\",\n" +
            "        \"S02Q08_intervention_checkbox\": false,\n" +
            "        \"S02Q09\": \"\",\n" +
            "        \"S02Q09_COMMENT\": \"\",\n" +
            "        \"S02Q09_intervention_checkbox\": false,\n" +
            "        \"S01Q02\": \"H\",\n" +
            "        \"S01Q02_COMMENT\": \"overall needs rating, updates.\",\n" +
            "        \"S02Q10\": \"aaaa\",\n" +
            "        \"S03Q01\": \"\",\n" +
            "        \"S03Q01_COMMENT\": \"\",\n" +
            "        \"S03Q02\": \"\",\n" +
            "        \"S03Q02_COMMENT\": \"\",\n" +
            "        \"S03Q03\": \"\",\n" +
            "        \"S03Q03_COMMENT\": \"\",\n" +
            "        \"S03Q04\": \"\",\n" +
            "        \"S03Q04_COMMENT\": \"\",\n" +
            "        \"S03Q05\": \"\",\n" +
            "        \"S03Q05_COMMENT\": \"\",\n" +
            "        \"S03Q06\": \"\",\n" +
            "        \"S03Q06_COMMENT\": \"\",\n" +
            "        \"S03Q07\": \"\",\n" +
            "        \"S03Q07_COMMENT\": \"\",\n" +
            "        \"S03Q08\": \"\",\n" +
            "        \"S03Q08_COMMENT\": \"\",\n" +
            "        \"S03Q09\": \"\",\n" +
            "        \"S03Q09_COMMENT\": \"\",\n" +
            "        \"S03Q10\": \"\",\n" +
            "        \"S03Q10_COMMENT\": \"\",\n" +
            "        \"S03Q11\": \"\",\n" +
            "        \"S01Q03\": \"M\",\n" +
            "        \"S01Q03_COMMENT\": \"<p>overall risk rating updates again</p>\",\n" +
            "        \"S01Q01\": \"L\",\n" +
            "        \"S01Q01_COMMENT\": \"<p>supervision rating updates.</p>\",\n" +
            "        \"S02Q01_intervention_datagrid\": [\n" +
            "            {\n" +
            "                \"S02Q01_intervention_desc\": \"aaaaaaa bbb ccc\",\n" +
            "                \"S02Q01_intervention_type\": \"EMPA\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"S02Q01_intervention_desc\": \"aasdfsdfsdafsdaf\",\n" +
            "                \"S02Q01_intervention_type\": \"FARE\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"responsivity\": {\n" +
            "            \"PRRE\": true,\n" +
            "            \"PRRE_desc\": \"previous responses to dsfsdf...\",\n" +
            "            \"BACO\": true,\n" +
            "            \"MHCF\": true,\n" +
            "            \"MHCF_desc\": \" safsdfsdafsdafsdfasdf\",\n" +
            "            \"BACO_desc\": \"Barriers\"\n" +
            "        },\n" +
            "        \"PLAN_SUMMARY_TXT\": \"sdfsdfasd 1\",\n" +
            "        \"COMMENT_TXT\": \"reassessmnt asdsfsd 1\"\n" +
            "    },\n" +
            "    \"clientFormId\": 389821\n" +
            "}";

    @Inject
    ClientFormSaveService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    ObjectMapper objectMapper = new ObjectMapper();



    @Test
    @DisplayName("Success: Form CRNA is cloned")
    public void testCloneCrnaForm() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(CRNA_FORM_TYPE, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(createCodeTable(CRNA_FORM_TYPE, BigDecimal.ONE.toPlainString())));
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn(DATA_ONE);
        Mockito.when(obridgeClientService.saveClientFormAnswers(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.any())).thenReturn("");

        BigDecimal result = sut.cloneClientForm(new CloneFormRequest("TEST", BigDecimal.ONE, BigDecimal.ONE, true), "TEST@idir", "1");

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Success: Form SARA is cloned")
    public void testCloneSaraForm() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(createCodeTable(SARA_FORM_TYPE, BigDecimal.ONE.toPlainString())));
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn(DATA_ONE);
        Mockito.when(obridgeClientService.saveClientFormAnswers(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.any())).thenReturn("");

        BigDecimal result = sut.cloneClientForm(new CloneFormRequest("TEST", BigDecimal.ONE, BigDecimal.ONE, true), "TEST@idir", "1");

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Success: Form SARA with child is cloned")
    public void testCloneSaraChildForm() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, BigDecimal.ONE));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(createCodeTable(SARA_FORM_TYPE, BigDecimal.ONE.toPlainString())));
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn(DATA_ONE);
        Mockito.when(obridgeClientService.saveClientFormAnswers(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.any())).thenReturn("");

        BigDecimal result = sut.cloneClientForm(new CloneFormRequest("TEST", BigDecimal.ONE, BigDecimal.ONE, true), "TEST@idir", "1");

        Assertions.assertEquals(BigDecimal.ONE, result);
        
    }

    @Test
    @DisplayName("Success: Form Acute is cloned")
    public void testCloneAcuteForm() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(ACUTE_FORM_TYPE, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(createCodeTable(ACUTE_FORM_TYPE, BigDecimal.ONE.toPlainString())));
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn(DATA_ONE);
        Mockito.when(obridgeClientService.saveClientFormAnswers(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.any())).thenReturn("");

        BigDecimal result = sut.cloneClientForm(new CloneFormRequest("TEST", BigDecimal.ONE, BigDecimal.ONE, true), "TEST@idir", "1");

        Assertions.assertEquals(BigDecimal.ONE, result);

    }


    @Test
    @DisplayName("Success: Form Static99R is cloned")
    public void testCloneStatic99RChildForm() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(STATIC99R_FORM_TYPE, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(createCodeTable(STATIC99R_FORM_TYPE, BigDecimal.ONE.toPlainString())));
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn(DATA_ONE);
        Mockito.when(obridgeClientService.saveClientFormAnswers(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.any())).thenReturn("");

        BigDecimal result = sut.cloneClientForm(new CloneFormRequest("TEST", BigDecimal.ONE, BigDecimal.ONE, true), "TEST@idir", "1");

        Assertions.assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Error: Form Type Invalid")
    public void testCloneCannotClone() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(createCodeTable(SARA_FORM_TYPE, BigDecimal.TEN.toPlainString())));
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn(DATA_ONE);
        Mockito.when(obridgeClientService.saveClientFormAnswers(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.any())).thenReturn("");

        Assertions.assertThrows(CCCMException.class, () -> sut.cloneClientForm(new CloneFormRequest("TEST", BigDecimal.ONE, BigDecimal.ONE, false), "NOTTEST@idir", "1"));

    }


    @Test
    @DisplayName("Error: Form Type Invalid")
    public void testCloneFormTypeInvalid() throws IOException {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientForm(SARA_FORM_TYPE, null));
        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(obridgeClientService.getFormTypes(Mockito.any())).thenReturn(Collections.singletonList(createCodeTable(SARA_FORM_TYPE, BigDecimal.TEN.toPlainString())));
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.any(), Mockito.any())).thenReturn(DATA_ONE);
        Mockito.when(obridgeClientService.saveClientFormAnswers(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.any())).thenReturn("");

        Assertions.assertThrows(CCCMException.class, () -> sut.cloneClientForm(new CloneFormRequest("TEST", BigDecimal.ONE, BigDecimal.ONE, true), "TEST@idir", "1"));

    }


    private ClientFormSummary createClientForm(String module, BigDecimal relatedFormId) {

        ClientFormSummary clientFormSummary = new ClientFormSummary();

        clientFormSummary.setFormTypeId(BigDecimal.ONE);
        clientFormSummary.setMostRecent(true);
        clientFormSummary.setModule(module);
        clientFormSummary.setCreatedBy("TEST");
        clientFormSummary.setRelatedClientFormId(relatedFormId);
        return clientFormSummary;

    }

    private CodeTable createCodeTable(String module, String code) {
        return new CodeTable(
                code,
                module
        );
    }

}
