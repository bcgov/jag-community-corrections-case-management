package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSummary;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

@RequestScoped
public class FormDataServiceImpl implements FormDataService {


    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Override
    public FormDetails getForm(BigDecimal formId, boolean includeAnswers) {
        return obridgeClientService.getForm(formId, includeAnswers);
    }

    /**
     * Get form summaries from obridge service
     * @param module the module name e.g CRNA, SARA, etc..
     * @param latestOnly only get the latest incarnation of a form
     * @return {@link List<FormSummary>}
     */
    @Override
    public List<FormSummary> getFormSummaries(String module, boolean latestOnly) {
        return obridgeClientService.getFormSummaries(module, latestOnly);
    }

    @Override
    public String getFormDecorator(String identifier) {
        return obridgeClientService.getFormDecorator(identifier);
    }
}
