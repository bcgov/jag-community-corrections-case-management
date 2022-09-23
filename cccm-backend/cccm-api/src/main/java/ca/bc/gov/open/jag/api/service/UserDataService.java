package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;


public interface UserDataService {

    Code getDefaultLocation(String user);

    CodeList getLocations(String user);

    String getOracleId(String user);

}
