package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;

public interface UserDataService {

    Code getDefaultLocation(String user);

}
