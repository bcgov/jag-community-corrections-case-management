package ca.bc.gov.open.jag.api.model.data;

import java.time.LocalDate;

public class CentreDashboard {

    private String clientName;
    private String clientNo;
    private LocalDate dischargeRtcDate;
    private LocalDate nextCourtDate;
    private String orderStatus;
    private String supLevel;
    private LocalDate CRNACompDate;
    private LocalDate CMRPCompDate;
    private LocalDate CMRPDueDate;
    private Integer itrp;
    private Integer rvo;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public LocalDate getDischargeRtcDate() {
        return dischargeRtcDate;
    }

    public void setDischargeRtcDate(LocalDate dischargeRtcDate) {
        this.dischargeRtcDate = dischargeRtcDate;
    }

    public LocalDate getNextCourtDate() {
        return nextCourtDate;
    }

    public void setNextCourtDate(LocalDate nextCourtDate) {
        this.nextCourtDate = nextCourtDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSupLevel() {
        return supLevel;
    }

    public void setSupLevel(String supLevel) {
        this.supLevel = supLevel;
    }

    public LocalDate getCRNACompDate() {
        return CRNACompDate;
    }

    public void setCRNACompDate(LocalDate CRNACompDate) {
        this.CRNACompDate = CRNACompDate;
    }

    public LocalDate getCMRPCompDate() {
        return CMRPCompDate;
    }

    public void setCMRPCompDate(LocalDate CMRPCompDate) {
        this.CMRPCompDate = CMRPCompDate;
    }

    public LocalDate getCMRPDueDate() {
        return CMRPDueDate;
    }

    public void setCMRPDueDate(LocalDate CMRPDueDate) {
        this.CMRPDueDate = CMRPDueDate;
    }

    public Integer getItrp() {
        return itrp;
    }

    public void setItrp(Integer itrp) {
        this.itrp = itrp;
    }

    public Integer getRvo() {
        return rvo;
    }

    public void setRvo(Integer rvo) {
        this.rvo = rvo;
    }
}
