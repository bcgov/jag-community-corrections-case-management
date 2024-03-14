package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSummary;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

@RequestScoped
public class FormDataServiceImpl implements FormDataService {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(FormDataServiceImpl.class));

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Override
    public FormDetails getForm(BigDecimal formId, boolean includeAnswers) {

        logger.debug("Form Details {} includeAnswers {}", formId, includeAnswers);

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

        logger.debug("Form Summaries {} latestOnly {}", module, latestOnly);

        return obridgeClientService.getFormSummaries(module, latestOnly);

    }

    @Override
    public String getFormDecorator(String identifier) {

        logger.debug("Form Decorators {}", identifier);

        return obridgeClientService.getFormDecorator(identifier);

    }
}
