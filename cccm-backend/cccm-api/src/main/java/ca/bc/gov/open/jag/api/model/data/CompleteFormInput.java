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
}
