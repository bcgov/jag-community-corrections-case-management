package ca.bc.gov.open.jag.api.service.dataservice.client;

import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Rating;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;

import static ca.bc.gov.open.jag.api.Keys.HIGH;

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

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(createForm(LocalDate.now().minusDays(59)));
        Mockito.when(obridgeClientService.getProfileById(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(createClientProfile());

        ClientFormSummary result = sut.getClientFormSummary(BigDecimal.ONE, "", "1");

        Assertions.assertEquals("CRNA", result.getModule());
        Assertions.assertFalse(result.getLocked());
        Assertions.assertEquals(HIGH, result.getSupervisionRating());
        Assertions.assertEquals("TEST", result.getClientCustodyLocation());

    }

    @Test
    @DisplayName("Success: should return clients form locked")
    public void testGetClientFormsMerge() {

        Mockito.when(obridgeClientService.getClientFormSummary(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(createForm(LocalDate.now().minusDays(61)));
        Mockito.when(obridgeClientService.getProfileById(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(createClientProfile());

        ClientFormSummary result = sut.getClientFormSummary(BigDecimal.ONE, "", "1");

        Assertions.assertEquals("CRNA", result.getModule());
        Assertions.assertTrue(result.getLocked());
        Assertions.assertEquals(HIGH, result.getSupervisionRating());
        Assertions.assertEquals("TEST", result.getClientCustodyLocation());

    }

    private ClientFormSummary createForm(LocalDate createdDate) {

        ClientFormSummary form1 = new ClientFormSummary();
        form1.setId(BigDecimal.ONE);
        form1.setCreatedDate(createdDate);
        form1.setRelatedClientFormId(BigDecimal.TEN);
        form1.setUpdatedDate(LocalDate.now());
        form1.setSupervisionRating(HIGH);

        Rating rating = new Rating();
        rating.setFormType("CRNA");
        rating.setText(HIGH);

        form1.getRatings().add(rating);
        form1.setModule("CRNA");

        return form1;

    }

    private ClientProfile createClientProfile() {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setInternalLocation("TEST");
        return clientProfile;
    }

}
