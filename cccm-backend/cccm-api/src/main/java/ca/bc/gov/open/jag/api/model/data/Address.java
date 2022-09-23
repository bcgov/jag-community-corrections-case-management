package ca.bc.gov.open.jag.api.model.data;

public class Address {

    private String fullAddress;
    private String expiryDate;

    public String getFullAddress() {
        return fullAddress;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
