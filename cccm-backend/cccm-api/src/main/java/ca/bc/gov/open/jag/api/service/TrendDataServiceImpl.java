package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class TrendDataServiceImpl implements TrendDataService {

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Override
    public List<TrendAnalysisConfig> getTrendConfig() {
        return obridgeClientService.getTrendTypes();
    }

    @Override
    public List<LabelValuePair> getTrendFactors(String trendType) {
        return obridgeClientService.getTrendFactors(trendType);
    }

    @Override
    public TrendFormData getChartData(TrendFilterInput trendInput) {
        return obridgeClientService.getClientTrendData(trendInput);
    }
}
