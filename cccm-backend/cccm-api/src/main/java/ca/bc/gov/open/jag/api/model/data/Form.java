package ca.bc.gov.open.jag.api.model.data;

import java.time.LocalDate;

public class Form {

    private String formType;
    private String assessmentStatus;
    private String status;
    private LocalDate updateDate;
    private String createdLocation;
    private String completedBy;
    private String supervisionRating;
    private String crnaRating;
    private String saraRating;

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getAssessmentStatus() {
        return assessmentStatus;
    }

    public void setAssessmentStatus(String assessmentStatus) {
        this.assessmentStatus = assessmentStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreatedLocation() {
        return createdLocation;
    }

    public void setCreatedLocation(String createdLocation) {
        this.createdLocation = createdLocation;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public String getSupervisionRating() {
        return supervisionRating;
    }

    public void setSupervisionRating(String supervisionRating) {
        this.supervisionRating = supervisionRating;
    }

    public String getCrnaRating() {
        return crnaRating;
    }

    public void setCrnaRating(String crnaRating) {
        this.crnaRating = crnaRating;
    }

    public String getSaraRating() {
        return saraRating;
    }

    public void setSaraRating(String saraRating) {
        this.saraRating = saraRating;
    }

}
