

package ca.bc.gov.open.jag.api.trend;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.TrendDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.TrendApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LabelValuePair;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendAnalysisConfig;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendFilterInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendFormData;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TrendApiImpl implements TrendApi {

    @Inject
    ClientDataService clientDataService;

    @Inject
    TrendDataService trendDataService;



    @Override
    @Transactional
    @RolesAllowed("form-view")
    public List<TrendAnalysisConfig> getConfigOptionsUsingGET() {
        return trendDataService.getTrendConfig();
    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public List<LabelValuePair> getTrendFactorsUsingGET(String type) {
        return trendDataService.getTrendFactors(type);
    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public TrendFormData getChartDataUsingPOST(TrendFilterInput trendInput) {
        return trendDataService.getChartData(trendInput);
    }




}
