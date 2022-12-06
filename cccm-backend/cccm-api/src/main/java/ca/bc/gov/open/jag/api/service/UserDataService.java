package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.*;

import java.math.BigDecimal;
import java.util.List;


public interface UserDataService {

    Code getDefaultLocation(String user);

    CodeList getLocations(String user);

    String getOracleId(String user);

    List<PODashboard> getPODashboard(String user, BigDecimal location);

    List<SupervisorDashboard> getSupervisorDashboard(String user, BigDecimal location);

    SupervisorDashboardDetails getSupervisorDashboardDetails(String userId, String location);

    LogonResult logonUser(String user, String locationType);

}
