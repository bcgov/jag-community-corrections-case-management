package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.FormRequest;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DataServiceImpl implements DataService {

    private static final Logger logger = Logger.getLogger(String.valueOf(DataService.class));

    private final ObjectMapper objectMapper;

    public DataServiceImpl(ObjectMapper objectMapper) {
        objectMapper.findAndRegisterModules();
        this.objectMapper = objectMapper;
    }

    @Override
    public FormDetails getFormRequest(FormRequest formRequest) throws CCCMException {

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

}
