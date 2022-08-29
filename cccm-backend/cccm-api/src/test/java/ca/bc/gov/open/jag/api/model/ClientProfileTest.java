package ca.bc.gov.open.jag.api.model;

import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
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
        sut.setFinalOrderExpiryDt(Date.valueOf(testDate));
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
        sut.setFederalParolOffice("TEST");
        sut.setIsBiometricEnrolled("TEST");
        sut.seteServicesStatus("TEST");
        sut.setBiometricStatus("TEST");
        sut.seteReporting("TEST");

        Assertions.assertEquals("TEST", sut.getSecondaryManager());
        Assertions.assertEquals("TEST", sut.getStatus());
        Assertions.assertEquals("TEST", sut.getSupervisionLevel());
        Assertions.assertEquals("TEST", sut.getOrders());
        Assertions.assertEquals(Date.valueOf(testDate), sut.getFinalOrderExpiryDt());
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
        Assertions.assertEquals("TEST", sut.getFederalParolOffice());
        Assertions.assertEquals("TEST", sut.getIsBiometricEnrolled());
        Assertions.assertEquals("TEST", sut.geteServicesStatus());
        Assertions.assertEquals("TEST", sut.getBiometricStatus());
        Assertions.assertEquals("TEST", sut.geteReporting());

    }

}
