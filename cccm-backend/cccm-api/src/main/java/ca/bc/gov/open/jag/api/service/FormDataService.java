package ca.bc.gov.open.jag.api.service;


import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSummary;

import java.math.BigDecimal;
import java.util.List;

public interface FormDataService {

    FormDetails getForm(BigDecimal formId, boolean includeAnswers);

    List<FormSummary> getFormSummaries(String module, boolean latestOnly);

    /**
     * Get a form decorator block (e.g. intervention json)
     * @param identifier e.g. intervention_panel
     * @return JSON resource contents
     */
    String getFormDecorator(String identifier);
}
