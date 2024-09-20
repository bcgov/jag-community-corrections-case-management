package ca.bc.gov.open.jag.api.model.data;

import java.time.LocalDate;

public class ClientDates {

    private LocalDate pddDate;
    private LocalDate crnaCompleteDate;

    public LocalDate getPddDate() {
        return pddDate;
    }

    public LocalDate getCrnaCompleteDate() {
        return crnaCompleteDate;
    }

    public void setPddDate(LocalDate pddDate) {
        this.pddDate = pddDate;
    }

    public void setCrnaCompleteDate(LocalDate crnaCompleteDate) {
        this.crnaCompleteDate = crnaCompleteDate;
    }

}
