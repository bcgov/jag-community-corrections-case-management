package ca.bc.gov.open.jag.api.service;


import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;

import java.math.BigDecimal;

public interface DataService {

    FormDetails getFormById(BigDecimal id);

}
