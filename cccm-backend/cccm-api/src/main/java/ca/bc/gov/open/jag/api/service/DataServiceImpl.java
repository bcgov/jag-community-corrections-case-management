package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SideCards;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
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
    public FormDetails getFormById(BigDecimal id) throws CCCMException {

        logger.log(Level.INFO, "Fetching form details");

        //This is only for testing. Will
        if (id.equals(BigDecimal.valueOf(999))) {

            throw new CCCMException("Form not found", CCCMErrorCode.RECORDNOTFOUND);
        }

        try {

            return objectMapper.readValue(Paths.get("sample_data.json").toFile(), FormDetails.class);

        } catch (Exception ex) {

             throw new CCCMException("Error loading json data", CCCMErrorCode.DATALOADERROR);

        }

    }

    @Override
    public SideCards getSideCardsById(BigDecimal id) throws CCCMException {

        logger.log(Level.INFO, "Fetching side card");

        try {

            return objectMapper.readValue(Paths.get("sampleSideCard.json").toFile(), SideCards.class);

        } catch (Exception ex) {

            throw new CCCMException("Error loading json data", CCCMErrorCode.DATALOADERROR);

        }
    }

}
