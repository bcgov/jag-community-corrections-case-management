package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.LabelValuePair;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendAnalysisConfig;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendFilterInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendFormData;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class TrendDataServiceImpl implements TrendDataService {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(TrendDataServiceImpl.class));

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Override
    public List<TrendAnalysisConfig> getTrendConfig() {

        return obridgeClientService.getTrendTypes();

    }

    @Override
    public List<LabelValuePair> getTrendFactors(String trendType) {

        logger.debug("Trend Factors {}", trendType);

        return obridgeClientService.getTrendFactors(trendType);

    }

    @Override
    public TrendFormData getChartData(TrendFilterInput trendInput) {

        logger.debug("Char Data {}", trendInput);

        return obridgeClientService.getClientTrendData(trendInput);

    }
}
