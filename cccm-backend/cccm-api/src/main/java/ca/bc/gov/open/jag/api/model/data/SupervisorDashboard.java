package ca.bc.gov.open.jag.api.model.data;

public class SupervisorDashboard {

    private String poName;
    private String idirId;
    private Integer activeAdmin;
    private Integer adminClosed;
    private Integer bAL;
    private Integer high;
    private Integer medium;
    private Integer low;
    private Integer unknown;
    private Integer overDueRNAs;
    private Integer activeReports;
    private Integer notRequired;

    public Integer getNotRequired() {
        return notRequired;
    }

    public void setNotRequired(Integer notRequired) {
        this.notRequired = notRequired;
    }

    public String getPoName() {
        return poName;
    }

    public void setPoName(String poName) {
        this.poName = poName;
    }

    public Integer getActiveAdmin() {
        return activeAdmin;
    }

    public void setActiveAdmin(Integer activeAdmin) {
        this.activeAdmin = activeAdmin;
    }

    public Integer getAdminClosed() {
        return adminClosed;
    }

    public void setAdminClosed(Integer adminClosed) {
        this.adminClosed = adminClosed;
    }

    public Integer getbAL() {
        return bAL;
    }

    public void setbAL(Integer bAL) {
        this.bAL = bAL;
    }

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public Integer getMedium() {
        return medium;
    }

    public void setMedium(Integer medium) {
        this.medium = medium;
    }

    public Integer getLow() {
        return low;
    }

    public void setLow(Integer low) {
        this.low = low;
    }

    public Integer getUnknown() {
        return unknown;
    }

    public void setUnknown(Integer unknown) {
        this.unknown = unknown;
    }

    public Integer getOverDueRNAs() {
        return overDueRNAs;
    }

    public void setOverDueRNAs(Integer overDueRNAs) {
        this.overDueRNAs = overDueRNAs;
    }

    public Integer getActiveReports() {
        return activeReports;
    }

    public void setActiveReports(Integer activeReports) {
        this.activeReports = activeReports;
    }

    public String getIdirId() {
        return idirId;
    }

    public void setIdirId(String idirId) {
        this.idirId = idirId;
    }
}
