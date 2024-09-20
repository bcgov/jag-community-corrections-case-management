package ca.bc.gov.open.jag.api.model;

import ca.bc.gov.open.jag.api.model.data.PODashboard;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

@QuarkusTest
public class PODashboardTest {

    private static final String POPDESG = "POPDESG";
    private static final String CLIENT_IN = "Y";
    private static final String SUPERVISION_LEVEL = "SUPER_LEVEL";
    private static final String MOED = "MOED";
    private static final LocalDate FOED = LocalDate.now();
    private static final LocalDate RNA_COMPLETE = LocalDate.now();
    private static final String CLIENT_NO = "123";
    private static final String CLIENT_NAME = "ClientName";
    private static final String RNA_CD = "RNACD";
    private static final String RETY_CD = "RETY_CD";
    private static final String RNAS_CD = "RNASCD";
    private static final LocalDate COMPLETION_DATE = LocalDate.now();
    private static final String COMMENT = "COMMENT";
    private static final String ISSUES_OUTSTANDING_YN = "ISSUES";
    private static final String FILE_REVIEW_COMPLETE_YN = "Y";
    private static final String DUE_NEXT = "DUENEXT";
    private static final LocalDate DUE_DATE = LocalDate.now();

    @Test
    @DisplayName("Test Dashboard Model")
    public void testDashboardModel() {

        PODashboard poDashboard = new PODashboard();
        poDashboard.setActiveDocuments(1);
        poDashboard.setAlerts(2);
        poDashboard.setPopDesignations(POPDESG);
        poDashboard.setClientIn(CLIENT_IN);
        poDashboard.setSupervisionLevel(SUPERVISION_LEVEL);
        poDashboard.setMOED(MOED);
        poDashboard.setFOED(FOED);
        poDashboard.setRNACompleted(RNA_COMPLETE);
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

        Assertions.assertEquals(poDashboard.getActiveDocuments(), 1);
        Assertions.assertEquals(poDashboard.getAlerts(), 2);
        Assertions.assertEquals(poDashboard.getPopDesignations(), POPDESG);
        Assertions.assertEquals(poDashboard.getClientIn(), CLIENT_IN);
        Assertions.assertEquals(poDashboard.getSupervisionLevel(), SUPERVISION_LEVEL);
        Assertions.assertEquals(poDashboard.getMOED(), MOED);
        Assertions.assertEquals(poDashboard.getFOED(), FOED);
        Assertions.assertEquals(poDashboard.getRNACompleted(), RNA_COMPLETE);
        Assertions.assertEquals(poDashboard.getIndiId(), BigDecimal.ONE);
        Assertions.assertEquals(poDashboard.getClientNo(), CLIENT_NO);
        Assertions.assertEquals(poDashboard.getClientId(), BigDecimal.ONE);
        Assertions.assertEquals(poDashboard.getClientName(), CLIENT_NAME);
        Assertions.assertEquals(poDashboard.getClinId(), BigDecimal.ONE);
        Assertions.assertEquals(poDashboard.getRnaCd(), RNA_CD);
        Assertions.assertEquals(poDashboard.getRetyCd(), RETY_CD);
        Assertions.assertEquals(poDashboard.getRnasCd(), RNAS_CD);
        Assertions.assertEquals(poDashboard.getCompletionDate(), COMPLETION_DATE);
        Assertions.assertEquals(poDashboard.getComment(), COMMENT);
        Assertions.assertEquals(poDashboard.getIssuesOutstandingYn(), ISSUES_OUTSTANDING_YN);
        Assertions.assertEquals(poDashboard.getComntySprvsrFileReviewId(), BigDecimal.ONE);
        Assertions.assertEquals(poDashboard.getFileReviewCompleteYn(), FILE_REVIEW_COMPLETE_YN);
        Assertions.assertEquals(poDashboard.getLocationId(), BigDecimal.ONE);
        Assertions.assertEquals(poDashboard.getDueNext(), DUE_NEXT);
        Assertions.assertEquals(poDashboard.getDueDate(), DUE_DATE);

    }

}
