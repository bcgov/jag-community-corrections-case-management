package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.mapper.FormMapper;
import ca.bc.gov.open.jag.api.model.data.Form;
import ca.bc.gov.open.jag.api.model.service.FormRequest;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSearchList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class FormDataServiceImpl implements FormDataService {

    private static final Logger logger = Logger.getLogger(String.valueOf(FormDataService.class));

    @Inject
    @RestClient
    SpeedmentClientService speedmentClientService;

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
    public FormSearchList formSearch(String clientNum, Boolean currentPeriod, String formTypeCd) {

        List<Form> forms;

        if (StringUtils.isBlank(formTypeCd)) {
            forms = speedmentClientService.getFormsByClient(clientNum);
        } else {
            forms = speedmentClientService.getFormsByClient(clientNum, formTypeCd);
        }

        return formMapper.toFormSearchList("", forms);

    }
}
