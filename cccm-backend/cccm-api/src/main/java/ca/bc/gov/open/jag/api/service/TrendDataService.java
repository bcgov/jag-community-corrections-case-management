package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.LabelValuePair;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendAnalysisConfig;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendFilterInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.TrendFormData;

import java.util.List;

public interface TrendDataService {

    /**
     * Get trend config - descriptions/names for display in UI
     * @return {@link List<TrendAnalysisConfig>}
     */
    List<TrendAnalysisConfig> getTrendConfig();

    /**
     * Get factors (questions for the given trend chart option)
     * @param trendType - e.g. sara-cp, crna-rt
     * @return {@link List<LabelValuePair>} for select option display
     */
    List<LabelValuePair> getTrendFactors(String trendType);

    /**
     * Get chart data based on filter input
     * @param trendInput
     * @return {@Link TrendChartData}
     */
    TrendFormData getChartData(TrendFilterInput trendInput);
}
