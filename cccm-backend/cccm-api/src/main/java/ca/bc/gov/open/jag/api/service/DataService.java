package ca.bc.gov.open.jag.api.service;


import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.FormRequest;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SideCards;

import java.math.BigDecimal;

public interface DataService {

    FormDetails getFormRequest(FormRequest formRequest) throws CCCMException;

    SideCards getSideCardsById(BigDecimal id) throws CCCMException;

}
