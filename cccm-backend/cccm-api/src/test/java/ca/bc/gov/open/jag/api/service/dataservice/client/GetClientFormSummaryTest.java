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

import static ca.bc.gov.open.jag.api.Keys.*;

@QuarkusTest
public class GetClientFormSummaryTest {

    @Inject
    ClientDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return clients form not locked")
    public void testGetClientFormsNoMerge() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.anyString(), Mockito.any())).thenReturn(createForm(LocalDate.now().minusDays(59)));

        ClientFormSummary result = sut.getClientFormSummary(BigDecimal.ONE, "");

        Assertions.assertEquals("CRNA", result.getModule());
        Assertions.assertFalse(result.getLocked());
        Assertions.assertEquals(HIGH, result.getSupervisionRating());

    }

    @Test
    @DisplayName("Success: should return clients form locked")
    public void testGetClientFormsMerge() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.anyString(), Mockito.any())).thenReturn(createForm(LocalDate.now().minusDays(61)));

        ClientFormSummary result = sut.getClientFormSummary(BigDecimal.ONE, "");

        Assertions.assertEquals("CRNA", result.getModule());
        Assertions.assertTrue(result.getLocked());
        Assertions.assertEquals(HIGH, result.getSupervisionRating());

    }

    private ClientFormSummary createForm(LocalDate createdDate) {

        ClientFormSummary form1 = new ClientFormSummary();
        form1.setId(BigDecimal.ONE);
        form1.setCreatedDate(createdDate);
        form1.setRelatedClientFormId(BigDecimal.TEN);
        form1.setUpdatedDate(LocalDate.now());
        form1.setSupervisionRating(HIGH);
        form1.getRatings().put("CRNA", HIGH);
        form1.setModule("CRNA");

        return form1;

    }

}
