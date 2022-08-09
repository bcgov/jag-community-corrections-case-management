package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.FormTypeList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationList;

public interface CodeTableService {

    FormTypeList formTypeCodes();

    LocationList locationCodes();

}
