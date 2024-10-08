package ca.bc.gov.open.jag.api.model;

import ca.bc.gov.open.jag.api.model.data.Client;
import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

@QuarkusTest
public class ClientProfileTest {

    @Test
    @DisplayName("Test Client Profile Model")
    public void testModel() {

        LocalDate testDate = LocalDate.now();

        ClientProfile sut = new ClientProfile();

        sut.setSecondaryManager("TEST");
        sut.setStatus("TEST");
        sut.setSupervisionLevel("TEST");
        sut.setOrders("TEST");
        sut.setFinalOrderExpiryDt(testDate);
        sut.setOrderEffectiveDt(testDate);
        sut.setReleaseReason("TEST");
        sut.setInstitutionstatus("TEST");
        sut.setClientinoutyn("TEST");
        sut.setClientType("TEST");
        sut.setInternalLocation("TEST");
        sut.setOutLocation("TEST");
        sut.setOutReason("TEST");
        sut.setIaStatus("TEST");
        sut.setPopDesignation("TEST");
        sut.setIcayraSecurity("TEST");
        sut.setIcayraSecurityStatus("TEST");
        sut.setWarrants(new ArrayList<>());
        sut.setAlerts(new ArrayList<>());
        sut.setPrograms(new ArrayList<>());
        sut.setFederalParolOffice("TEST");
        sut.setIsBiometricEnrolled("TEST");
        sut.seteServicesStatus("TEST");
        sut.setBiometricStatus("TEST");
        sut.seteReporting("TEST");
        sut.setPossibleParoleDt(testDate);
        sut.setProbableDischargeDt(testDate);
        sut.setFinalWarrantExpiryDt(testDate);
        sut.setClient(new Client());
        sut.setOffense("TEST");
        sut.setNextCourtDate(testDate);
        sut.setIaClassification("TEST");
        sut.setCustody("TEST");
        sut.setNextDueDt(testDate);
        sut.setUcmpDesignation("TEST");


        Assertions.assertEquals("TEST", sut.getSecondaryManager());
        Assertions.assertEquals("TEST", sut.getStatus());
        Assertions.assertEquals("TEST", sut.getSupervisionLevel());
        Assertions.assertEquals("TEST", sut.getOrders());
        Assertions.assertEquals(testDate, sut.getFinalOrderExpiryDt());
        Assertions.assertEquals(testDate, sut.getOrderEffectiveDt());
        Assertions.assertEquals("TEST", sut.getReleaseReason());
        Assertions.assertEquals("TEST", sut.getInstitutionstatus());
        Assertions.assertEquals("TEST", sut.getClientinoutyn());
        Assertions.assertEquals("TEST", sut.getClientType());
        Assertions.assertEquals("TEST", sut.getInternalLocation());
        Assertions.assertEquals("TEST", sut.getOutLocation());
        Assertions.assertEquals("TEST", sut.getOutReason());
        Assertions.assertEquals("TEST", sut.getIaStatus());
        Assertions.assertEquals("TEST", sut.getPopDesignation());
        Assertions.assertEquals("TEST", sut.getIcayraSecurity());
        Assertions.assertEquals("TEST", sut.getIcayraSecurityStatus());
        Assertions.assertEquals(0, sut.getWarrants().size());
        Assertions.assertEquals(0, sut.getAlerts().size());
        Assertions.assertEquals(0, sut.getPrograms().size());
        Assertions.assertEquals("TEST", sut.getFederalParolOffice());
        Assertions.assertEquals("TEST", sut.getIsBiometricEnrolled());
        Assertions.assertEquals("TEST", sut.geteServicesStatus());
        Assertions.assertEquals("TEST", sut.getBiometricStatus());
        Assertions.assertEquals("TEST", sut.geteReporting());
        Assertions.assertEquals(testDate, sut.getPossibleParoleDt());
        Assertions.assertEquals(testDate, sut.getFinalWarrantExpiryDt());
        Assertions.assertEquals(testDate, sut.getProbableDischargeDt());
        Assertions.assertNotNull(sut.getClient());
        Assertions.assertEquals("TEST", sut.getOffense());
        Assertions.assertEquals("TEST", sut.getCustody());
        Assertions.assertEquals("TEST", sut.getIaClassification());
        Assertions.assertEquals(testDate, sut.getNextCourtDate());
        Assertions.assertEquals(testDate, sut.getNextDueDt());
        Assertions.assertEquals("TEST", sut.getUcmpDesignation());

    }

}
