package ca.bc.gov.open.jag.api.service.dataservice.user;

import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.PODashboard;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class GetPoDashboardTest {

    private static final String POPDESG = "POPDESG";
    private static final String UCMPPDESG = "UCMPPDESG";
    private static final String CLIENT_IN = "Y";
    private static final String SUPERVISION_LEVEL = "SUPER_LEVEL";
    private static final String MOED = "MOED";
    private static final String FOED = "FOED";
    private static final String CLIENT_NO = "123";
    private static final String CLIENT_NAME = "ClientName";
    private static final String RNA_CD = "RNACD";
    private static final String RETY_CD = "RETY_CD";
    private static final String RNAS_CD = "RNASCD";
    private static final String COMPLETION_DATE = "COMPLETIONDATE";
    private static final String COMMENT = "COMMENT";
    private static final String ISSUES_OUTSTANDING_YN = "ISSUES";
    private static final String FILE_REVIEW_COMPLETE_YN = "Y";
    private static final String DUE_NEXT = "DUENEXT";
    private static final String DUE_DATE = "DUE_DATE";
    @Inject
    UserDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return data")
    public void testPODashboard() {

        Mockito.when(obridgeClientService.getPODashboard(Mockito.any(), Mockito.any())).thenReturn(createPOResult());

        List<PODashboard> result = sut.getPODashboard("test@idir", BigDecimal.ONE);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(new BigDecimal(2), result.get(0).getAlerts());
        Assertions.assertEquals(CLIENT_NO, result.get(0).getClientNum());
        Assertions.assertEquals(CLIENT_NAME, result.get(0).getClientName());
        Assertions.assertEquals(UCMPPDESG, result.get(0).getDesignations());
        Assertions.assertEquals(CLIENT_IN, result.get(0).getInCustody());
        Assertions.assertEquals(DUE_DATE, result.get(0).getDueDate());
        Assertions.assertEquals(DUE_NEXT, result.get(0).getDueNext());
        Assertions.assertEquals(FOED, result.get(0).getOrderExpiryDate());
        Assertions.assertEquals(COMPLETION_DATE, result.get(0).getRnaCompletedDate());
        Assertions.assertEquals(SUPERVISION_LEVEL, result.get(0).getSupervisionRating());

    }

    private List<ca.bc.gov.open.jag.api.model.data.PODashboard> createPOResult() {

        ca.bc.gov.open.jag.api.model.data.PODashboard poDashboard = new ca.bc.gov.open.jag.api.model.data.PODashboard();
        poDashboard.setActiveDocuments(1);
        poDashboard.setAlerts(2);
        poDashboard.setPopDesignations(POPDESG);
        poDashboard.setUcmpDesignations(UCMPPDESG);
        poDashboard.setClientIn(CLIENT_IN);
        poDashboard.setSupervisionLevel(SUPERVISION_LEVEL);
        poDashboard.setMOED(MOED);
        poDashboard.setFOED(FOED);
        poDashboard.setRNACompleted(COMPLETION_DATE);
        poDashboard.setIndiId(BigDecimal.ONE);
        poDashboard.setClientNo(CLIENT_NO);
        poDashboard.setClientId(BigDecimal.ONE);
        poDashboard.setClientName(CLIENT_NAME);
        poDashboard.setClinId(BigDecimal.ONE);
        poDashboard.setRnaCd(RNA_CD);
        poDashboard.setRetyCd(RETY_CD);
        poDashboard.setRnasCd(RNAS_CD);
        poDashboard.setCompletionDate(COMPLETION_DATE);
        poDashboard.setComment(COMMENT);
        poDashboard.setIssuesOutstandingYn(ISSUES_OUTSTANDING_YN);
        poDashboard.setComntySprvsrFileReviewId(BigDecimal.ONE);
        poDashboard.setFileReviewCompleteYn(FILE_REVIEW_COMPLETE_YN);
        poDashboard.setLocationId(BigDecimal.ONE);
        poDashboard.setDueNext(DUE_NEXT);
        poDashboard.setDueDate(DUE_DATE);

        return Collections.singletonList(poDashboard);

    }

}
