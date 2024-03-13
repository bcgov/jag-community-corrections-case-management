package ca.bc.gov.open.jag.api.dashboard;

import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.DashboardsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CentreDashboard;
import ca.bc.gov.open.jag.cccm.api.openapi.model.PODashboard;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SupervisorDashboard;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SupervisorDashboardDetails;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

@RequestScoped
public class DashboardsApiImpl implements DashboardsApi {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(DashboardsApiImpl.class));

    @Inject
    UserDataService userDataService;

    @Inject
    @Claim(standard = Claims.preferred_username)
    String username;

    @Override
    @RolesAllowed("po-manage")
    public List<SupervisorDashboard> getSupervisorDashboard(String xLocationId, BigDecimal locationId) {

        logger.info("Supervisor dashboard request");

        return userDataService.getSupervisorDashboard(username, locationId);

    }

    @Override
    @RolesAllowed("po-manage")
    public SupervisorDashboardDetails getSupervisorDashboardDetails(String idirId, String xLocationId, BigDecimal locationId) {

        logger.info("Supervisor dashboard details request");

        return userDataService.getSupervisorDashboardDetails(idirId, locationId);

    }

    @Override
    @RolesAllowed("client-search")
    public List<PODashboard> getPODashboard(String idirId, BigDecimal locationId) {

        logger.info("Dashboard request received");

        return userDataService.getPODashboard(idirId, locationId);

    }

    @Override
    @RolesAllowed("client-search")
    public List<CentreDashboard> getCentreDashboard(BigDecimal locationId) {

        logger.info("Centre Dashboard request received");

        return userDataService.getCentreDashboard(locationId);

    }

}
