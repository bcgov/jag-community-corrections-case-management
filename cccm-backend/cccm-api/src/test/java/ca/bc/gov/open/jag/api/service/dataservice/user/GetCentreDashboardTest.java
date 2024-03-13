package ca.bc.gov.open.jag.api.service.dataservice.user;

import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CentreDashboard;
import ca.bc.gov.open.jag.cccm.api.openapi.model.PODashboard;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class GetCentreDashboardTest {

    private static final String CLIENT_NAME = "TEST";
    private static final String CLIENT_NO = "123";
    private static final String CMRP_COMP_DATE = "CMRP_COMP_DATE";
    private static final String CMRP_DUE_DATE = "CMRP_DUE_DATE";
    private static final String CRNA_COMP_DATE = "CRNA_COMP_DATE";
    private static final String SUP_LEVEL = "SUP_LEVEL";
    private static final String DISCHARGE_DATE = "DISCHARGE_DATE";
    private static final String NEXT_COURT_DATE = "NEXT_COURT_DATE";
    @Inject
    UserDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return data")
    public void testPODashboard() {

        Mockito.when(obridgeClientService.getCentreDashboard(Mockito.any())).thenReturn(createCentreResult());

        List<CentreDashboard> result = sut.getCentreDashboard(BigDecimal.ONE);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(CLIENT_NO, result.get(0).getClientNum());
        Assertions.assertEquals(CLIENT_NAME, result.get(0).getClientName());
        Assertions.assertEquals(CMRP_COMP_DATE, result.get(0).getCmRPCompDate());
        Assertions.assertEquals(CMRP_DUE_DATE, result.get(0).getCmRPDueDate());
        Assertions.assertEquals(CRNA_COMP_DATE, result.get(0).getCrNACompDate());
        Assertions.assertEquals(SUP_LEVEL, result.get(0).getSupervisionLevel());
        Assertions.assertEquals(DISCHARGE_DATE, result.get(0).getDischargeRtcDate());
        Assertions.assertEquals(NEXT_COURT_DATE, result.get(0).getNextCourtDate());

    }

    private List<ca.bc.gov.open.jag.api.model.data.CentreDashboard> createCentreResult() {

        ca.bc.gov.open.jag.api.model.data.CentreDashboard centreDashboard = new ca.bc.gov.open.jag.api.model.data.CentreDashboard();

        centreDashboard.setClientNo(CLIENT_NO);
        centreDashboard.setClientName(CLIENT_NAME);
        centreDashboard.setCMRPCompDate(CMRP_COMP_DATE);
        centreDashboard.setCMRPDueDate(CMRP_DUE_DATE);
        centreDashboard.setCRNACompDate(CRNA_COMP_DATE);
        centreDashboard.setSupLevel(SUP_LEVEL);
        centreDashboard.setDischargeRtcDate(DISCHARGE_DATE);
        centreDashboard.setNextCourtDate(NEXT_COURT_DATE);

        return Collections.singletonList(centreDashboard);

    }

}
