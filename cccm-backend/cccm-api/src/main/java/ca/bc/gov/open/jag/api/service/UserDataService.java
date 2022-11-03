package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.PODashboard;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SupervisorDashboard;

import java.math.BigDecimal;
import java.util.List;


public interface UserDataService {

    Code getDefaultLocation(String user);

    CodeList getLocations(String user);

    String getOracleId(String user);

    List<PODashboard> getPODashboard(String user, BigDecimal location);

    List<SupervisorDashboard> getSupervisorDashboard(String user, BigDecimal location);

}
