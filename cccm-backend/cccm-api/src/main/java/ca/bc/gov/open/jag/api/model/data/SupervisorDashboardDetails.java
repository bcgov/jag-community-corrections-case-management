package ca.bc.gov.open.jag.api.model.data;

public class SupervisorDashboardDetails {

    private String PCM;
    private String SCM;
    private Integer SMO;
    private Integer closedIncomplete;
    private Integer expiringThirty;
    private Integer notRequired;

    public String getPCM() {
        return PCM;
    }

    public void setPCM(String PCM) {
        this.PCM = PCM;
    }

    public String getSCM() {
        return SCM;
    }

    public void setSCM(String SCM) {
        this.SCM = SCM;
    }

    public Integer getSMO() {
        return SMO;
    }

    public void setSMO(Integer SMO) {
        this.SMO = SMO;
    }

    public Integer getClosedIncomplete() {
        return closedIncomplete;
    }

    public void setClosedIncomplete(Integer closedIncomplete) {
        this.closedIncomplete = closedIncomplete;
    }

    public Integer getExpiringThirty() {
        return expiringThirty;
    }

    public void setExpiringThirty(Integer expiringThirty) {
        this.expiringThirty = expiringThirty;
    }

    public Integer getNotRequired() {
        return notRequired;
    }

    public void setNotRequired(Integer notRequired) {
        this.notRequired = notRequired;
    }
}
