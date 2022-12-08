package ca.bc.gov.open.jag.model;

public class User {

    private String groupName;
    private String groupDescription;
    private String oracleId;
    private String idirId;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getOracleId() {
        return oracleId;
    }

    public void setOracleId(String oracleId) {
        this.oracleId = oracleId;
    }

    public String getIdirId() {
        return idirId;
    }

    public void setIdirId(String idirId) {
        this.idirId = idirId;
    }

}
