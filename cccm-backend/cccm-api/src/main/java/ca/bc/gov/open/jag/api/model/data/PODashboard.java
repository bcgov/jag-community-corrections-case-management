package ca.bc.gov.open.jag.api.model.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PODashboard {

    private Integer activeDocuments;
    private Integer alerts;
    private String popDesignations;
    private String ucmpDesignations;
    private String clientIn;
    private String supervisionLevel;
    private String MOED;
    private LocalDate FOED;
    private LocalDate RNACompleted;
    private BigDecimal indiId;
    private String clientNo;
    private BigDecimal clientId;
    private String  clientName;
    private BigDecimal clinId;
    private String rnaCd;
    private String retyCd;
    private String rnasCd;
    private LocalDate completionDate;
    private String comment;
    private String issuesOutstandingYn;
    private BigDecimal comntySprvsrFileReviewId;
    private String fileReviewCompleteYn;
    private BigDecimal  locationId;
    private String dueNext;
    private LocalDate dueDate;

    public Integer getActiveDocuments() {
        return activeDocuments;
    }

    public void setActiveDocuments(Integer activeDocuments) {
        this.activeDocuments = activeDocuments;
    }

    public Integer getAlerts() {
        return alerts;
    }

    public void setAlerts(Integer alerts) {
        this.alerts = alerts;
    }

    public String getPopDesignations() {
        return popDesignations;
    }

    public void setPopDesignations(String popDesignations) {
        this.popDesignations = popDesignations;
    }

    public String getClientIn() {
        return clientIn;
    }

    public void setClientIn(String clientIn) {
        this.clientIn = clientIn;
    }

    public String getSupervisionLevel() {
        return supervisionLevel;
    }

    public void setSupervisionLevel(String supervisionLevel) {
        this.supervisionLevel = supervisionLevel;
    }

    public String getMOED() {
        return MOED;
    }

    public void setMOED(String MOED) {
        this.MOED = MOED;
    }

    public LocalDate getFOED() {
        return FOED;
    }

    public void setFOED(LocalDate FOED) {
        this.FOED = FOED;
    }

    public LocalDate getRNACompleted() {
        return RNACompleted;
    }

    public void setRNACompleted(LocalDate RNACompleted) {
        this.RNACompleted = RNACompleted;
    }

    public BigDecimal getIndiId() {
        return indiId;
    }

    public void setIndiId(BigDecimal indiId) {
        this.indiId = indiId;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public BigDecimal getClientId() {
        return clientId;
    }

    public void setClientId(BigDecimal clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public BigDecimal getClinId() {
        return clinId;
    }

    public void setClinId(BigDecimal clinId) {
        this.clinId = clinId;
    }

    public String getRnaCd() {
        return rnaCd;
    }

    public void setRnaCd(String rnaCd) {
        this.rnaCd = rnaCd;
    }

    public String getRetyCd() {
        return retyCd;
    }

    public void setRetyCd(String retyCd) {
        this.retyCd = retyCd;
    }

    public String getRnasCd() {
        return rnasCd;
    }

    public void setRnasCd(String rnasCd) {
        this.rnasCd = rnasCd;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIssuesOutstandingYn() {
        return issuesOutstandingYn;
    }

    public void setIssuesOutstandingYn(String issuesOutstandingYn) {
        this.issuesOutstandingYn = issuesOutstandingYn;
    }

    public BigDecimal getComntySprvsrFileReviewId() {
        return comntySprvsrFileReviewId;
    }

    public void setComntySprvsrFileReviewId(BigDecimal comntySprvsrFileReviewId) {
        this.comntySprvsrFileReviewId = comntySprvsrFileReviewId;
    }

    public String getFileReviewCompleteYn() {
        return fileReviewCompleteYn;
    }

    public void setFileReviewCompleteYn(String fileReviewCompleteYn) {
        this.fileReviewCompleteYn = fileReviewCompleteYn;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public void setLocationId(BigDecimal locationId) {
        this.locationId = locationId;
    }

    public String getDueNext() {
        return dueNext;
    }

    public void setDueNext(String dueNext) {
        this.dueNext = dueNext;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getUcmpDesignations() {
        return ucmpDesignations;
    }

    public void setUcmpDesignations(String ucmpDesignations) {
        this.ucmpDesignations = ucmpDesignations;
    }

}
