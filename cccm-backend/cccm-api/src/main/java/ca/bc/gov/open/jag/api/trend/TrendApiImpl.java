

package ca.bc.gov.open.jag.api.trend;

import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.TrendDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.TrendApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LabelValuePair;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendAnalysisConfig;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendFilterInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendFormData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TrendApiImpl implements TrendApi {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(TrendApi.class));

    @Inject
    TrendDataService trendDataService;

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
