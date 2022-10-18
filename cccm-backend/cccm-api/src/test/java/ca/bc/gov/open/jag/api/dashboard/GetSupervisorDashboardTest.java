package ca.bc.gov.open.jag.api.dashboard;

import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SupervisorDashboard;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class GetSupervisorDashboardTest {

    @Inject
    DashboardsApiImpl sut;

    @InjectMock
    UserDataService userDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "po-manage")
    @DisplayName("200: should return photo")
    public void testGetSupervisorDashboardEndpoint() {

        Mockito.when(userDataService.getSupervisorDashboard(any(), any())).thenReturn(createSupervisorList());

        List<SupervisorDashboard> result = sut.getSupervisorDashboard(null, "TEST");

        Assertions.assertEquals(1, result.size());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getSupervisorDashboard(null,"TEST"));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getSupervisorDashboard(null,"TEST"));

    }

    private List<SupervisorDashboard> createSupervisorList() {

        SupervisorDashboard supervisorDashboard = new SupervisorDashboard();

        return Collections.singletonList(supervisorDashboard);

    }

}
