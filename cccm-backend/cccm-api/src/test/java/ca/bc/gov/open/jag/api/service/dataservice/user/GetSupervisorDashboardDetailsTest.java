package ca.bc.gov.open.jag.api.service.dataservice.user;

import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SupervisorDashboardDetails;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;

@QuarkusTest
public class GetSupervisorDashboardDetailsTest {

    private static final String TEST_STRING = "TEST";

    @Inject
    UserDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return data")
    public void testSupervisorDashboard() {

        Mockito.when(obridgeClientService.getSupervisorDashboardDetails(Mockito.any(), Mockito.any())).thenReturn(createDetails());

        SupervisorDashboardDetails result = sut.getSupervisorDashboardDetails("test@idir", BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result.getClosedIncomplete());
        Assertions.assertEquals(BigDecimal.ONE, result.getDueSeven());
        Assertions.assertEquals(BigDecimal.ONE, result.getExpiringThirty());
        Assertions.assertEquals(BigDecimal.ONE, result.getSmo());
        Assertions.assertEquals(TEST_STRING, result.getPcm());
        Assertions.assertEquals(TEST_STRING, result.getScm());

    }

    private ca.bc.gov.open.jag.api.model.data.SupervisorDashboardDetails createDetails() {

        ca.bc.gov.open.jag.api.model.data.SupervisorDashboardDetails supervisorDashboardDetails = new ca.bc.gov.open.jag.api.model.data.SupervisorDashboardDetails();
        supervisorDashboardDetails.setClosedIncomplete(1);
        supervisorDashboardDetails.setNotRequired(1);
        supervisorDashboardDetails.setExpiringThirty(1);
        supervisorDashboardDetails.setPCM(TEST_STRING);
        supervisorDashboardDetails.setSCM(TEST_STRING);
        supervisorDashboardDetails.setSMO(1);
        return supervisorDashboardDetails;

    }

}
