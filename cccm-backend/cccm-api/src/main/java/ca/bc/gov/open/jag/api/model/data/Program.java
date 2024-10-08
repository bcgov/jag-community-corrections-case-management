package ca.bc.gov.open.jag.api.model.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Program {

    private BigDecimal clientId;
    private String programTypeCd;
    private String programName;
    private String locationDsc;
    private String status;
    private LocalDate referralDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String outcome;
    private String outcomeReason;
    private BigDecimal programAttendanceId;
    private BigDecimal locationId;
    private String lotyCd;
    private BigDecimal  programSessionId;
    private LocalDate programSessionStartDate;

    public BigDecimal getClientId() {
        return clientId;
    }

    public void setClientId(BigDecimal clientId) {
        this.clientId = clientId;
    }

    public String getProgramTypeCd() {
        return programTypeCd;
    }

    public void setProgramTypeCd(String programTypeCd) {
        this.programTypeCd = programTypeCd;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getLocationDsc() {
        return locationDsc;
    }

    public void setLocationDsc(String locationDsc) {
        this.locationDsc = locationDsc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(LocalDate referralDate) {
        this.referralDate = referralDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getOutcomeReason() {
        return outcomeReason;
    }

    public void setOutcomeReason(String outcomeReason) {
        this.outcomeReason = outcomeReason;
    }

    public BigDecimal getProgramAttendanceId() {
        return programAttendanceId;
    }

    public void setProgramAttendanceId(BigDecimal programAttendanceId) {
        this.programAttendanceId = programAttendanceId;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public void setLocationId(BigDecimal locationId) {
        this.locationId = locationId;
    }

    public String getLotyCd() {
        return lotyCd;
    }

    public void setLotyCd(String lotyCd) {
        this.lotyCd = lotyCd;
    }

    public BigDecimal getProgramSessionId() {
        return programSessionId;
    }

    public void setProgramSessionId(BigDecimal programSessionId) {
        this.programSessionId = programSessionId;
    }

    public LocalDate getProgramSessionStartDate() {
        return programSessionStartDate;
    }

    public void setProgramSessionStartDate(LocalDate programSessionStartDate) {
        this.programSessionStartDate = programSessionStartDate;
    }
}
