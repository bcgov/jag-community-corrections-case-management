package ca.bc.gov.open.jag.api.service.dataservice.client;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;
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
import java.util.Arrays;
import java.util.List;

import static ca.bc.gov.open.jag.api.Keys.*;

@QuarkusTest
public class ClientFormSearchTest {

    @Inject
    ClientDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return clients forms")
    public void testGetClientFormsNoMerge() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(createNonRelatedList());

        List<ClientFormSummary> result = sut.clientFormSearch("", false, "All");

        Assertions.assertEquals(2, result.size());

    }

    @Test
    @DisplayName("Success: should return clients forms with merges")
    public void testGetClientFormsMerge() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(createRelatedList());

        List<ClientFormSummary> result = sut.clientFormSearch("", false, "All");

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("CRNA-SARA", result.get(0).getModule());
        Assertions.assertEquals(HIGH, result.get(0).getSupervisionRating());
        Assertions.assertEquals("CRNA-SARA", result.get(2).getModule());
        Assertions.assertEquals(MEDIUM, result.get(2).getSupervisionRating());

    }

    @Test
    @DisplayName("Success: should return clients forms CRNA no relations")
    public void testGetClientFormsCRNA() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(createNonRelatedList());

        List<ClientFormSummary> result = sut.clientFormSearch("", false, "CRNA");

        Assertions.assertEquals(1, result.size());

    }

    @Test
    @DisplayName("Success: should return clients forms SARA ")
    public void testGetClientFormsSARA() {

        Mockito.when(obridgeClientService.getClientForms(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(createNonRelatedList());

        List<ClientFormSummary> result = sut.clientFormSearch("", false, "SARA");

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
        form1.setRelatedClientFormId(BigDecimal.TEN);
        form1.setUpdatedDate(LocalDate.now());
        form1.setSupervisionRating(HIGH);
        form1.setModule("CRNA");

        ClientFormSummary form2 = new ClientFormSummary();
        form2.setId(BigDecimal.TEN);
        form2.setRelatedClientFormId(BigDecimal.ONE);
        form2.setUpdatedDate(LocalDate.now().minusDays(1));
        form2.setSupervisionRating(MEDIUM);
        form2.setModule("SARA");

        ClientFormSummary form3 = new ClientFormSummary();
        form3.setId(BigDecimal.ZERO);
        form3.setModule("CRNA");

        ClientFormSummary form4 = new ClientFormSummary();
        form4.setId(BigDecimal.valueOf(123));
        form4.setRelatedClientFormId(BigDecimal.valueOf(321));
        form4.setUpdatedDate(LocalDate.now());
        form4.setSupervisionRating(MEDIUM);
        form4.setModule("SARA");

        ClientFormSummary form5 = new ClientFormSummary();
        form5.setId(BigDecimal.valueOf(321));
        form5.setRelatedClientFormId(BigDecimal.valueOf(123));
        form5.setUpdatedDate(LocalDate.now().minusDays(1));
        form5.setSupervisionRating(LOW);
        form5.setModule("CRNA");

        return Arrays.asList(form1, form2, form3, form4, form5);

    }


}