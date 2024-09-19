package ca.bc.gov.open.jag.api.dashboard;

import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CentreDashboard;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.security.TestSecurity;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class GetCentreDashboardTest {

    private static final String CLIENT_NAME = "TEST";
    private static final String CLIENT_NO = "123";
    private static final LocalDate CMRP_COMP_DATE = LocalDate.now();
    private static final LocalDate CMRP_DUE_DATE = LocalDate.now();
    private static final LocalDate CRNA_COMP_DATE = LocalDate.now();
    private static final String SUP_LEVEL = "SUP_LEVEL";
    private static final LocalDate DISCHARGE_DATE = LocalDate.now();
    private static final LocalDate NEXT_COURT_DATE = LocalDate.now();
    private static final BigDecimal ALERT_COUNT = BigDecimal.ONE;

    @Inject
    DashboardsApiImpl sut;

    @InjectMock
    UserDataService userDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return dashboard")
    public void testGetPODashboardLocationEndpoint() {



        Mockito.when(userDataService.getCentreDashboard(any())).thenReturn(createCentreList());

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
        Assertions.assertEquals(ALERT_COUNT, result.get(0).getItrpCount());
        Assertions.assertEquals(ALERT_COUNT, result.get(0).getRvoCount());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getDefaultLocationsTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getPODashboard("123", BigDecimal.ONE));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getDefaultLocationTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getPODashboard("123",BigDecimal.ONE));

    }

    private List<CentreDashboard> createCentreList() {

        CentreDashboard centreDashboard = new CentreDashboard();

        centreDashboard.setClientNum(CLIENT_NO);
        centreDashboard.setClientName(CLIENT_NAME);
        centreDashboard.setCmRPCompDate(CMRP_COMP_DATE);
        centreDashboard.setCmRPDueDate(CMRP_DUE_DATE);
        centreDashboard.setCrNACompDate(CRNA_COMP_DATE);
        centreDashboard.setSupervisionLevel(SUP_LEVEL);
        centreDashboard.setDischargeRtcDate(DISCHARGE_DATE);
        centreDashboard.setNextCourtDate(NEXT_COURT_DATE);
        centreDashboard.setRvoCount(ALERT_COUNT);
        centreDashboard.setItrpCount(ALERT_COUNT);


        return Collections.singletonList(centreDashboard);

    }

}
