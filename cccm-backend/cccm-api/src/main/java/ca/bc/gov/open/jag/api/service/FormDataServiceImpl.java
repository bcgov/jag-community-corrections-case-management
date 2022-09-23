package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.mapper.FormMapper;
import ca.bc.gov.open.jag.api.model.data.Form;
import ca.bc.gov.open.jag.api.model.service.FormRequest;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSearchList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class FormDataServiceImpl implements FormDataService {

    private static final Logger logger = Logger.getLogger(String.valueOf(FormDataService.class));

//    @Inject
//    @RestClient
//    SpeedmentClientService speedmentClientService;

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    FormMapper formMapper;

    private final ObjectMapper objectMapper;

    public FormDataServiceImpl(ObjectMapper objectMapper) {
        objectMapper.findAndRegisterModules();
        this.objectMapper = objectMapper;
    }

    @Override
    public FormDetails formRequest(FormRequest formRequest) throws CCCMException {

        logger.log(Level.INFO, "Fetching form details");

        //This is only for testing. Will be removed when connected to database.
        if (formRequest.getFormId() != null && formRequest.getFormId().equals(BigDecimal.valueOf(999))) {
            throw new CCCMException("Form not found", CCCMErrorCode.RECORDNOTFOUND);
        } else if (formRequest.getFormType() != null && formRequest.getFormType().equals("NOTFOUND")) {
            throw new CCCMException("Form not found", CCCMErrorCode.RECORDNOTFOUND);
        }

        try {

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream sampleDataResource = loader.getResourceAsStream("sampleFormData.json");

            return objectMapper.readValue(sampleDataResource, FormDetails.class);

        } catch (Exception ex) {
            logger.severe(ex.getMessage());
             throw new CCCMException("Error loading json data", CCCMErrorCode.DATALOADERROR);

        }

    }



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
