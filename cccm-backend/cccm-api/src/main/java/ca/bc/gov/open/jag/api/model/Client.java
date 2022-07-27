package ca.bc.gov.open.jag.api.model;

import java.math.BigDecimal;

public class Client {

    private BigDecimal clientNo;
    private String clientName;
    private String currentNameYn;
    private String genderCode;
    private String birthDate;
    private String custodyLocation;
    private String communityLocation;

    public BigDecimal getClientNo() {
        return clientNo;
    }

    public void setClientNo(BigDecimal clientNo) {
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

}
