package ca.bc.gov.open.jag.api.service.dataservice.user;

import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SupervisorDashboard;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class GetSupervisorDashboardTest {

    private static final String TEST_PO = "TEST_PO";
    private static final int INT_TEST = 1;

    @Inject
    UserDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return data")
    public void testSupervisorDashboard() {

        Mockito.when(obridgeClientService.getSupervisorDashboard(Mockito.any(), Mockito.any())).thenReturn(createSupervisorResult());

        List<SupervisorDashboard> result = sut.getSupervisorDashboard("test@idir", BigDecimal.ONE);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(TEST_PO, result.get(0).getOfficer());
        Assertions.assertEquals(new BigDecimal(INT_TEST), result.get(0).getActiveAdmin());
        Assertions.assertEquals(new BigDecimal(INT_TEST), result.get(0).getActiveReports());
        Assertions.assertEquals(new BigDecimal(INT_TEST), result.get(0).getAdminClosed());
        Assertions.assertEquals(new BigDecimal(INT_TEST), result.get(0).getHigh());
        Assertions.assertEquals(new BigDecimal(INT_TEST), result.get(0).getMedium());
        Assertions.assertEquals(new BigDecimal(INT_TEST), result.get(0).getLow());
        Assertions.assertEquals(new BigDecimal(INT_TEST), result.get(0).getUnknown());
        Assertions.assertEquals(new BigDecimal(INT_TEST), result.get(0).getOverdue());
        Assertions.assertEquals(new BigDecimal(INT_TEST), result.get(0).getBal());

    }

    private List<ca.bc.gov.open.jag.api.model.data.SupervisorDashboard> createSupervisorResult() {

        ca.bc.gov.open.jag.api.model.data.SupervisorDashboard supervisorDashboard = new ca.bc.gov.open.jag.api.model.data.SupervisorDashboard();
        supervisorDashboard.setPoName(TEST_PO);
        supervisorDashboard.setActiveAdmin(INT_TEST);
        supervisorDashboard.setActiveReports(INT_TEST);
        supervisorDashboard.setAdminClosed(INT_TEST);
        supervisorDashboard.setbAL(INT_TEST);
        supervisorDashboard.setHigh(INT_TEST);
        supervisorDashboard.setMedium(INT_TEST);
        supervisorDashboard.setLow(INT_TEST);
        supervisorDashboard.setUnknown(INT_TEST);
        supervisorDashboard.setOverDueRNAs(INT_TEST);

        return Collections.singletonList(supervisorDashboard);

    }

}
