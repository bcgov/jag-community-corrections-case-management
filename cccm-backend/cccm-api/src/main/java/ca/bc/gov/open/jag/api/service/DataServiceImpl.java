package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DataServiceImpl implements DataService {

    private static final Logger logger = Logger.getLogger(String.valueOf(DataService.class));

    @Override
    public FormDetails getFormById(BigDecimal id) {

        logger.log(Level.INFO, "Fetching form details");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {

            return objectMapper.readValue(Paths.get("sample_data.json").toFile(), FormDetails.class);

        } catch (Exception ex) {

             return null;

        }

    }

}
