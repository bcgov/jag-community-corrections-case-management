package ca.bc.gov.open.jag.api.dashboard;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class GetSupervisorDashboardTest {

    @Inject
    DashboardsApiImpl sut;

    @Test
    @TestSecurity(user = "userOidc", roles = "po-manage")
    @DisplayName("200: should return photo")
    public void testGetSupervisorDashboardEndpoint() {

        Assertions.assertNull(sut.getSupervisorDashboard(null, "TEST"));


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


}
