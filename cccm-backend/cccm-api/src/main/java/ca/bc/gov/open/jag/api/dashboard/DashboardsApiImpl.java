package ca.bc.gov.open.jag.api.dashboard;

import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.DashboardsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.PODashboard;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SupervisorDashboard;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.jboss.logmanager.Level;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class DashboardsApiImpl implements DashboardsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(DashboardsApiImpl.class));

    @Inject
    UserDataService userDataService;

    @Inject
    @Claim(standard = Claims.preferred_username)
    String username;

    @Override
    @RolesAllowed("po-manage")
    public List<SupervisorDashboard> getSupervisorDashboard(String xLocationId, String userId) {
        logger.warning("This is not implemented");
        return userDataService.getSupervisorDashboard(username, xLocationId);

    }

    @Override
    @RolesAllowed("client-search")
    public List<PODashboard> getPODashboard(String xLocationId) {

        logger.log(Level.DEBUG, "Dashboard request received");

        return userDataService.getPODashboard(username, xLocationId);

    }

}
