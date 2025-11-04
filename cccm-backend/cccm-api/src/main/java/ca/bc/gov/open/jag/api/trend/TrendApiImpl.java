

package ca.bc.gov.open.jag.api.trend;

import ca.bc.gov.open.jag.api.service.TrendDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.TrendApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LabelValuePair;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendAnalysisConfig;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendFilterInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendFormData;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
@io.quarkus.arc.Unremovable
public class TrendApiImpl implements TrendApi {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(TrendApi.class));

    private final TrendDataService trendDataService;

    @Inject
    public TrendApiImpl(TrendDataService trendDataService) {
        this.trendDataService = trendDataService;
    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public List<TrendAnalysisConfig> getConfigOptionsUsingGET() {

        logger.info("Config Options Request");

        return trendDataService.getTrendConfig();

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public List<LabelValuePair> getTrendFactorsUsingGET(String type) {

        logger.info("Trend Factors Request");

        return trendDataService.getTrendFactors(type);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public TrendFormData getChartDataUsingPOST(TrendFilterInput trendInput) {

        logger.info("Chart Data Request");

        return trendDataService.getChartData(trendInput);

    }

}
