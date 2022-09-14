package ca.bc.gov.open.jag.api.service;


import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.service.FormRequest;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSearchList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSummary;

import java.math.BigDecimal;
import java.util.List;

public interface FormDataService {

    FormDetails formRequest(FormRequest formRequest) throws CCCMException;

    FormSearchList formSearch(String clientNum, Boolean currentPeriod, String formTypeCd);

    FormDetails getForm(BigDecimal formId, boolean includeAnswers);

    List<FormSummary> getFormSummaries(String module, boolean latestOnly);
}
