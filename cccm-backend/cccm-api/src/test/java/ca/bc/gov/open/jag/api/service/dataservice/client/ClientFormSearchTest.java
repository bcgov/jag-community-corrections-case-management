package ca.bc.gov.open.jag.api.service.dataservice.client;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Rating;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ca.bc.gov.open.jag.api.Keys.*;

@QuarkusTest
public class ClientFormSearchTest {

    @Inject
    ClientDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @InjectMock
    JsonWebToken jsonWebTokenMock;

    private static final String answersJson = "{\n" +
            "\t\"data\": {\n" +
            "\t\t\"S01Q01\": \"2AE\",\n" +
            "\t\t\"S01Q04\": \"4a\",\n" +
            "\t\t\"S01Q05\": \"L\",\n" +
            "\t\t\"S01Q03\": \"M\",\n" +
            "\t\t\"S01Q02\": \"L\"\n" +
            "\t},\n" +
            "\t\"clientFormId\": 392603\n" +
            "}";

    @Test
    @DisplayName("Success: should return clients recent forms")
    public void testGetClientFormsNoMerge() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(createRelatedList());
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(answersJson);

        Mockito.when(jsonWebTokenMock.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithSMO()));

        List<ClientFormSummary> result = sut.clientFormSearch("", false, true,"All", "1");

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals("Medium", result.get(2).getSupervisionRating());

    }

    @Test
    @DisplayName("Success: should return clients all forms")
    public void testGetClientFormsAllNoMerge() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(createNonRelatedList());
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(answersJson);

        Mockito.when(jsonWebTokenMock.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithSMO()));

        List<ClientFormSummary> result = sut.clientFormSearch("", false, false,"All", "1");

        Assertions.assertEquals(2, result.size());

    }

    @Test
    @DisplayName("Success: should return clients forms with merges")
    public void testGetClientFormsMerge() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(createRelatedList());
        Mockito.when(jsonWebTokenMock.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithSMO()));
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(answersJson);

        List<ClientFormSummary> result = sut.clientFormSearch("", false, false,"All", "1");

        Assertions.assertEquals(6, result.size());
        Assertions.assertEquals("CRNA-SARA", result.get(0).getModule());
        Assertions.assertTrue(result.get(0).getLocked());
        Assertions.assertEquals(HIGH, result.get(0).getSupervisionRating());
        Assertions.assertEquals("CRNA-SARA", result.get(1).getModule());
        Assertions.assertEquals(MEDIUM, result.get(1).getSupervisionRating());

    }

    @Test
    @DisplayName("Success: should return clients forms CRNA no relations")
    public void testGetClientFormsCRNA() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(createNonRelatedList());
        Mockito.when(jsonWebTokenMock.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithSMO()));
        List<ClientFormSummary> result = sut.clientFormSearch("", false, false,"CRNA", "1");

        Assertions.assertEquals(1, result.size());

    }

    @Test
    @DisplayName("Success: should return clients forms SARA ")
    public void testGetClientFormsSARA() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(createNonRelatedList());
        Mockito.when(jsonWebTokenMock.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithSMO()));
        List<ClientFormSummary> result = sut.clientFormSearch("", false, false,"SARA", "1");

        Assertions.assertEquals(1, result.size());


    }

    @Test
    @DisplayName("Success: should return clients forms ACUTE ")
    public void testGetClientFormsACUTE() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(createRelatedList());
        Mockito.when(jsonWebTokenMock.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithSMO()));
        List<ClientFormSummary> result = sut.clientFormSearch("", false, false,"ACUTE", "1");

        Assertions.assertEquals(1, result.size());

    }

    @Test
    @DisplayName("Success: should return clients forms STAT99R ")
    public void testGetClientFormsSTAT99R() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(createRelatedList());
        Mockito.when(jsonWebTokenMock.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithSMO()));
        List<ClientFormSummary> result = sut.clientFormSearch("", false, false,"STAT99R", "1");

        Assertions.assertEquals(1, result.size());

    }

    @Test
    @DisplayName("Success: should return clients forms without SMO")
    public void testGetClientFormsWithoutSMO() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(createRelatedList());
        Mockito.when(jsonWebTokenMock.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithoutSMO()));

        List<ClientFormSummary> result = sut.clientFormSearch("", false, false,"STAT99R", "1");

        Assertions.assertEquals(0, result.size());

    }

    @Test
    @DisplayName("Success: should return clients forms without SMO")
    public void testGetAllClientFormsWithoutSMO() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(createRelatedList());
        Mockito.when(jsonWebTokenMock.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithoutSMO()));

        List<ClientFormSummary> result = sut.clientFormSearch("", false, false,"ALL", "1");

        Assertions.assertEquals(3, result.size());

    }

    @Test
    @DisplayName("Success: should return clients forms Overall")
    public void testGetAllClientFormsOverall() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(createRelatedList());
        Mockito.when(jsonWebTokenMock.claim(Mockito.anyString())).thenReturn(Optional.of(getRolesWithSMO()));
        Mockito.when(obridgeClientService.getClientFormAnswers(Mockito.anyString(), Mockito.any(BigDecimal.class))).thenReturn(answersJson);

        List<ClientFormSummary> result = sut.clientFormSearch("", false, true,"SMO_OVERALL", "1");

        Assertions.assertEquals(1, result.size());

    }


    private List<ClientFormSummary> createNonRelatedList() {

        ClientFormSummary form1 = new ClientFormSummary();
        form1.setId(BigDecimal.ONE);
        form1.setModule("CRNA");

        ClientFormSummary form2 = new ClientFormSummary();
        form2.setId(BigDecimal.TEN);
        form2.setModule("SARA");

        return Arrays.asList(form1, form2);

    }

    private List<ClientFormSummary> createRelatedList() {

        ClientFormSummary form1 = new ClientFormSummary();
        form1.setId(BigDecimal.ONE);
        form1.setCreatedDate(LocalDate.now().minusDays(61));
        form1.setRelatedClientFormId(BigDecimal.TEN);
        form1.setOsuUpdateDate(LocalDate.now());
        form1.setSupervisionRating(HIGH);

        Rating rating1 = new Rating();
        rating1.setFormType("CRNA");
        rating1.setText(HIGH);

        form1.getRatings().add(rating1);
        form1.setMostRecent(false);
        form1.setModule("CRNA");

        ClientFormSummary form2 = new ClientFormSummary();
        form2.setId(BigDecimal.TEN);
        form2.setRelatedClientFormId(BigDecimal.ONE);
        form2.setMostRecent(true);
        form2.setOsuUpdateDate(LocalDate.now().minusDays(1));
        form2.setSupervisionRating(MEDIUM);

        Rating rating2 = new Rating();
        rating2.setFormType("SARA");
        rating2.setText(MEDIUM);

        form2.getRatings().add(rating2);
        form2.setModule("SARA");

        ClientFormSummary form3 = new ClientFormSummary();
        form3.setId(BigDecimal.ZERO);
        form3.setMostRecent(true);
        form3.setCreatedDate(LocalDate.now().minusDays(3));
        form3.setModule("CRNA");

        ClientFormSummary form4 = new ClientFormSummary();
        form4.setId(BigDecimal.valueOf(123));
        form4.setMostRecent(false);
        form4.setRelatedClientFormId(BigDecimal.valueOf(321));
        form4.setOsuUpdateDate(LocalDate.now());
        form4.setSupervisionRating(MEDIUM);

        Rating rating4 = new Rating();
        rating4.setFormType("SARA");
        rating4.setText(MEDIUM);

        form4.getRatings().add(rating4);
        form4.setModule("SARA");

        ClientFormSummary form5 = new ClientFormSummary();
        form5.setId(BigDecimal.valueOf(321));
        form5.setMostRecent(false);
        form5.setRelatedClientFormId(BigDecimal.valueOf(123));
        form5.setOsuUpdateDate(LocalDate.now().minusDays(1));
        form5.setSupervisionRating(LOW);

        Rating rating5 = new Rating();
        rating5.setFormType("CRNA");
        rating5.setText(LOW);

        form5.getRatings().add(rating5);
        form5.setModule("CRNA");

        ClientFormSummary form6 = new ClientFormSummary();
        form6.setId(BigDecimal.valueOf(321));
        form6.setOsuUpdateDate(LocalDate.now().minusDays(1));
        form6.setSupervisionRating(LOW);


        Rating rating6 = new Rating();
        rating6.setFormType("ACUTE");
        rating6.setText(LOW);

        form6.getRatings().add(rating6);
        form6.setMostRecent(true);
        form6.setModule("ACUTE");

        ClientFormSummary form7 = new ClientFormSummary();
        form7.setId(BigDecimal.valueOf(321));
        form7.setOsuUpdateDate(LocalDate.now().minusDays(1));
        form7.setSupervisionRating(LOW);

        Rating rating7 = new Rating();
        rating7.setFormType("STAT99R");
        rating7.setText(LOW);

        form7.getRatings().add(rating7);
        form7.setMostRecent(true);
        form7.setModule("STAT99R");

        ClientFormSummary form8 = new ClientFormSummary();
        form8.setId(BigDecimal.valueOf(329));
        form8.setOsuUpdateDate(LocalDate.now().minusDays(1));
        form8.setSupervisionRating(LOW);

        Rating rating8 = new Rating();
        rating8.setFormType("SMO_OVERALL");
        rating8.setText(LOW);

        form8.getRatings().add(rating8);
        form8.setMostRecent(true);
        form8.setModule("SMO_OVERALL");


        return Arrays.asList(form1, form2, form3, form4, form5, form6, form7, form8);

    }

    private JsonObject getRolesWithSMO() {
         JsonReader jsonReader = Json.createReader(new StringReader("{\n" +
                 "\t\"roles\": [\n" +
                 "\t\t\"smo-forms\",\n" +
                 "\t\t\"blarg\"\n" +
                 "\t]\n" +
                 "}"));
        return jsonReader.readObject();
    }

    private JsonObject getRolesWithoutSMO() {
        JsonReader jsonReader = Json.createReader(new StringReader("{\n" +
                "\t\"roles\": [\n" +
                "\t\t\"blarg\"\n" +
                "\t]\n" +
                "}"));
        return jsonReader.readObject();
    }

}
