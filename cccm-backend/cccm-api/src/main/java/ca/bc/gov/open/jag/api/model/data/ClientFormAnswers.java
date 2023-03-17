package ca.bc.gov.open.jag.api.model.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientFormAnswers {

    private BigDecimal clientFormId;
    private boolean complete;
    private LocalDate completedDate;
    private String comment;
    private boolean sealed;
    private boolean mostRecent;
    private String planSummary;
    private String formComments;
    private String sourcesContacted;

    private List<Answer> answers = new ArrayList<>();

    public BigDecimal getClientFormId() {
        return clientFormId;
    }

    public void setClientFormId(BigDecimal clientFormId) {
        this.clientFormId = clientFormId;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isSealed() {
        return sealed;
    }

    public void setSealed(boolean sealed) {
        this.sealed = sealed;
    }

    public boolean isMostRecent() {
        return mostRecent;
    }

    public void setMostRecent(boolean mostRecent) {
        this.mostRecent = mostRecent;
    }

    public String getPlanSummary() {
        return planSummary;
    }

    public void setPlanSummary(String planSummary) {
        this.planSummary = planSummary;
    }

    public String getFormComments() {
        return formComments;
    }

    public void setFormComments(String formComments) {
        this.formComments = formComments;
    }

    public String getSourcesContacted() {
        return sourcesContacted;
    }

    public void setSourcesContacted(String sourcesContacted) {
        this.sourcesContacted = sourcesContacted;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
