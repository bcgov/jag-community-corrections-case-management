package ca.bc.gov.open.jag.api.model.data;

public class Client {

    private String clientNo;
    private String clientName;
    private String currentName;
    private String currentNameYn;
    private String genderCode;
    private String birthDate;
    private String custodyLocation;
    private String communityLocation;
    private String caseManager;
    private String address;
    private String addressStatus;
    private String addressType;
    private String addressExpiry;
    private String alias;
    private String sealed;

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCurrentNameYn() {
        return currentNameYn;
    }

    public void setCurrentNameYn(String currentNameYn) {
        this.currentNameYn = currentNameYn;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCustodyLocation() {
        return custodyLocation;
    }

    public void setCustodyLocation(String custodyLocation) {
        this.custodyLocation = custodyLocation;
    }

    public String getCommunityLocation() {
        return communityLocation;
    }

    public void setCommunityLocation(String communityLocation) {
        this.communityLocation = communityLocation;
    }

    public String getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(String caseManager) {
        this.caseManager = caseManager;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSealed() {
        return sealed;
    }

    public void setSealed(String sealed) {
        this.sealed = sealed;
    }

    public String getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(String addressStatus) {
        this.addressStatus = addressStatus;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddressExpiry() {
        return addressExpiry;
    }

    public void setAddressExpiry(String addressExpiry) {
        this.addressExpiry = addressExpiry;
    }
}
