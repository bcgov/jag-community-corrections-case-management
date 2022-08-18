package ca.bc.gov.open.jag.api.dashboard;

import ca.bc.gov.open.jag.cccm.api.openapi.DashboardsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SupervisorDashboard;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DashboardsApiImpl implements DashboardsApi {

    @Override
    @RolesAllowed("po-manage")
    public SupervisorDashboard getSupervisorDashboard(String userId) {
        return null;
    }

}
