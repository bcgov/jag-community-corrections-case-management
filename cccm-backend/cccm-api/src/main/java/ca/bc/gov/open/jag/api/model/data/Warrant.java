package ca.bc.gov.open.jag.api.model.data;

import java.time.LocalDate;

public class Warrant {

    private String courtFileNumber;
    private String charge;
    private LocalDate issuedDate;

    public String getCourtFileNumber() {
        return courtFileNumber;
    }

    public void setCourtFileNumber(String courtFileNumber) {
        this.courtFileNumber = courtFileNumber;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }
}
