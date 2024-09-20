package ca.bc.gov.open.jag.api.model.data;

import java.time.LocalDate;

public class Alert {

    private String description;
    private LocalDate effectiveDate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

}
