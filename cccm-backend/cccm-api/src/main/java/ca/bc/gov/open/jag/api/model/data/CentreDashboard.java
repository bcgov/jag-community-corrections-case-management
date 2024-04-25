package ca.bc.gov.open.jag.api.model.data;

public class CentreDashboard {

    private String clientName;
    private String clientNo;
    private String dischargeRtcDate;
    private String nextCourtDate;
    private String orderStatus;
    private String supLevel;
    private String CRNACompDate;
    private String CMRPCompDate;
    private String CMRPDueDate;
    private Integer itrp;
    private Integer rvo;

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

    public String getDischargeRtcDate() {
        return dischargeRtcDate;
    }

    public void setDischargeRtcDate(String dischargeRtcDate) {
        this.dischargeRtcDate = dischargeRtcDate;
    }

    public String getNextCourtDate() {
        return nextCourtDate;
    }

    public void setNextCourtDate(String nextCourtDate) {
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

    public String getCRNACompDate() {
        return CRNACompDate;
    }

    public void setCRNACompDate(String CRNACompDate) {
        this.CRNACompDate = CRNACompDate;
    }

    public String getCMRPCompDate() {
        return CMRPCompDate;
    }

    public void setCMRPCompDate(String CMRPCompDate) {
        this.CMRPCompDate = CMRPCompDate;
    }

    public String getCMRPDueDate() {
        return CMRPDueDate;
    }

    public void setCMRPDueDate(String CMRPDueDate) {
        this.CMRPDueDate = CMRPDueDate;
    }
}
