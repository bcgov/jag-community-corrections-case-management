package ca.bc.gov.open.jag.api.sidecard;

import ca.bc.gov.open.jag.api.service.DataService;
import ca.bc.gov.open.jag.cccm.api.openapi.SideCardsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SideCards;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.logging.Logger;

@ApplicationScoped
public class SideCardsApiImpl implements SideCardsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(SideCardsApiImpl.class));

    @Inject
    DataService dataService;

    @Override
    public SideCards getSideCards() {

        logger.info("Get side cards request received");
        
        return dataService.getSideCardsById(BigDecimal.ONE);

    }
}
