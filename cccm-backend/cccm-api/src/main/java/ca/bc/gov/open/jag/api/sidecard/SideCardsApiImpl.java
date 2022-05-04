package ca.bc.gov.open.jag.api.sidecard;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.service.DataService;
import ca.bc.gov.open.jag.cccm.api.openapi.SideCardsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SideCards;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;

@ApplicationScoped
public class SideCardsApiImpl implements SideCardsApi {

    @Inject
    DataService dataService;

    @Override
    public SideCards getSideCards() {

        return dataService.getSideCardsById(BigDecimal.ONE);

    }
}
