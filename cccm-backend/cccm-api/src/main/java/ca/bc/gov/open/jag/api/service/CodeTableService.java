package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.lookup.CodeTableType;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;

public interface CodeTableService {

    CodeList getCodes(CodeTableType type);

}
