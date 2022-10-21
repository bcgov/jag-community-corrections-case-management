package ca.bc.gov.open.jag.api.model.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FormInput {

    private String clientNumber;
    private BigDecimal locationId;
    private BigDecimal formTypeId;

    private LocalDate completionDate;
    private String formLevelComments;
    private String sourcesContacted;
    private String planSummary;

    private BigDecimal clientFormId;
    private BigDecimal linkedClientFormId;

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public void setLocationId(BigDecimal locationId) {
        this.locationId = locationId;
    }

    public void setFormTypeId(BigDecimal formTypeId) {
        this.formTypeId = formTypeId;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public void setFormLevelComments(String formLevelComments) {
        this.formLevelComments = formLevelComments;
    }

    public void setSourcesContacted(String sourcesContacted) {
        this.sourcesContacted = sourcesContacted;
    }

    public void setPlanSummary(String planSummary) {
        this.planSummary = planSummary;
    }

    public void setClientFormId(BigDecimal clientFormId) {
        this.clientFormId = clientFormId;
    }

    public void setLinkedClientFormId(BigDecimal linkedClientFormId) {
        this.linkedClientFormId = linkedClientFormId;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public BigDecimal getFormTypeId() {
        return formTypeId;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public String getFormLevelComments() {
        return formLevelComments;
    }

    public String getSourcesContacted() {
        return sourcesContacted;
    }

    public String getPlanSummary() {
        return planSummary;
    }

    public BigDecimal getClientFormId() {
        return clientFormId;
    }

    public BigDecimal getLinkedClientFormId() {
        return linkedClientFormId;
    }
}
