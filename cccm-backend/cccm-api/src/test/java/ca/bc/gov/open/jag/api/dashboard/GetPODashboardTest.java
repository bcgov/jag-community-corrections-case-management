package ca.bc.gov.open.jag.api.dashboard;

import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.PODashboard;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class GetPODashboardTest {

    private static final String CLIENT_NAME = "TEST";
    private static final String DESIGNATIONS = "TEST DESG";
    private static final String IN_CUSTODY = "Y";
    private static final LocalDate DUE_DATE = LocalDate.now();
    private static final LocalDate ORDER_EXPIRY_DATE = LocalDate.now();
    private static final LocalDate RNA_COMPLETED_DATE = LocalDate.now();
    private static final String SUPERVISION_RATING = "SUPERVISIONRATING";

    @Inject
    DashboardsApiImpl sut;

    @InjectMock
    UserDataService userDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return dashboard")
    public void testGetPODashboardLocationEndpoint() {



        Mockito.when(userDataService.getPODashboard(any(), any())).thenReturn(createPOList());

        List<PODashboard> result = sut.getPODashboard("123", BigDecimal.ONE);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(BigDecimal.ONE, result.get(0).getAlerts());
        Assertions.assertEquals(CLIENT_NAME, result.get(0).getClientName());
        Assertions.assertEquals(DESIGNATIONS, result.get(0).getDesignations());
        Assertions.assertEquals(IN_CUSTODY, result.get(0).getInCustody());
        Assertions.assertEquals(DUE_DATE, result.get(0).getDueDate());
        Assertions.assertEquals("test", result.get(0).getDueNext());
        Assertions.assertEquals(ORDER_EXPIRY_DATE, result.get(0).getOrderExpiryDate());
        Assertions.assertEquals(RNA_COMPLETED_DATE, result.get(0).getRnaCompletedDate());
        Assertions.assertEquals(SUPERVISION_RATING, result.get(0).getSupervisionRating());

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

    private List<PODashboard> createPOList() {

        PODashboard poDashboard = new PODashboard();

        poDashboard.setAlerts(BigDecimal.ONE);
        poDashboard.setClientName(CLIENT_NAME);
        poDashboard.setDesignations(DESIGNATIONS);
        poDashboard.setInCustody(IN_CUSTODY);
        poDashboard.setDueDate(DUE_DATE);
        poDashboard.setDueNext("test");
        poDashboard.setOrderExpiryDate(ORDER_EXPIRY_DATE);
        poDashboard.setRnaCompletedDate(RNA_COMPLETED_DATE);
        poDashboard.setSupervisionRating(SUPERVISION_RATING);

        return Collections.singletonList(poDashboard);

    }

}
