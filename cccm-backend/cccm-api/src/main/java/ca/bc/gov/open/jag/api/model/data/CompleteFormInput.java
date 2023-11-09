package ca.bc.gov.open.jag.api.model.data;

import java.math.BigDecimal;


public class CompleteFormInput {

    private String clientNumber;
    private BigDecimal clientFormId;
    private String completeYn;
    private String oracleId;
    private String formLevelComments;
    private String sourcesContacted;
    private String planSummary;
    private String formType;
    private BigDecimal formTypeId;
    private String overallSupervision;
    private BigDecimal locationId;

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public BigDecimal getClientFormId() {
        return clientFormId;
    }

    public void setClientFormId(BigDecimal clientFormId) {
        this.clientFormId = clientFormId;
    }

    public String getCompleteYn() {
        return completeYn;
    }

    public void setCompleteYn(String completeYn) {
        this.completeYn = completeYn;
    }

    public String getOracleId() {
        return oracleId;
    }

    public void setOracleId(String oracleId) {
        this.oracleId = oracleId;
    }

    public String getFormLevelComments() {
        return formLevelComments;
    }

    public void setFormLevelComments(String formLevelComments) {
        this.formLevelComments = formLevelComments;
    }

    public String getSourcesContacted() {
        return sourcesContacted;
    }

    public void setSourcesContacted(String sourcesContacted) {
        this.sourcesContacted = sourcesContacted;
    }

    public String getPlanSummary() {
        return planSummary;
    }

    public void setPlanSummary(String planSummary) {
        this.planSummary = planSummary;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getOverallSupervision() {
        return overallSupervision;
    }

    public void setOverallSupervision(String overallSupervision) {
        this.overallSupervision = overallSupervision;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public void setLocationId(BigDecimal locationId) {
        this.locationId = locationId;
    }

    public BigDecimal getFormTypeId() {
        return formTypeId;
    }

    public void setFormTypeId(BigDecimal formTypeId) {
        this.formTypeId = formTypeId;
    }
}
