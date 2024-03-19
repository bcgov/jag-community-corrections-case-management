package ca.bc.gov.open.jag.api.dashboard;

import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SupervisorDashboardDetails;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class GetSupervisorDashboardDetailsTest {

    private static final String TEST_STRING = "TEST";

    @Inject
    DashboardsApiImpl sut;

    @InjectMock
    UserDataService userDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "po-manage")
    @DisplayName("200: should return dashboard")
    public void testGetSupervisorDashboardEndpoint() {

        Mockito.when(userDataService.getSupervisorDashboardDetails(any(), any())).thenReturn(createDetails());

        SupervisorDashboardDetails result = sut.getSupervisorDashboardDetails(null, "", null);

        Assertions.assertEquals(BigDecimal.ONE, result.getClosedIncomplete());
        Assertions.assertEquals(BigDecimal.ONE, result.getDueSeven());
        Assertions.assertEquals(BigDecimal.ONE, result.getExpiringThirty());
        Assertions.assertEquals(BigDecimal.ONE, result.getSmo());
        Assertions.assertEquals(TEST_STRING, result.getPcm());
        Assertions.assertEquals(TEST_STRING, result.getScm());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getSupervisorDashboardDetails(null, "", null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getSupervisorDashboardDetails(null, "", null));

    }

    private SupervisorDashboardDetails createDetails() {

        SupervisorDashboardDetails supervisorDashboardDetails = new SupervisorDashboardDetails();
        supervisorDashboardDetails.setClosedIncomplete(BigDecimal.ONE);
        supervisorDashboardDetails.setDueSeven(BigDecimal.ONE);
        supervisorDashboardDetails.setExpiringThirty(BigDecimal.ONE);
        supervisorDashboardDetails.setPcm(TEST_STRING);
        supervisorDashboardDetails.setScm(TEST_STRING);
        supervisorDashboardDetails.setSmo(BigDecimal.ONE);
        return supervisorDashboardDetails;

    }

}

