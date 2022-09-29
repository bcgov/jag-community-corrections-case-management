

package ca.bc.gov.open.jag.api.trend;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.TrendApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ChartDataSet;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LabelValuePair;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TrendApiImpl implements TrendApi {

    @Inject
    ClientDataService clientDataService;

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public ChartDataSet getChartDataUsingGET(String csNumber, String reportType) {
        return clientDataService.getClientChartData(reportType, csNumber);
    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public List<LabelValuePair> getClientFormFactorsUsingGET(String csNumber, String reportType) {
        return clientDataService.getClientFormFactors(reportType, csNumber);
    }
}
