package ca.bc.gov.open.jag.api.model.service;

public class ClientAddressSearch {

    private String addressType;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private Boolean expired;

    public ClientAddressSearch(String addressType, String address, String city, String province, String postalCode, Boolean expired) {
        this.addressType = addressType;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.expired = expired;
    }

    public String getAddressType() {
        return addressType;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Boolean getExpired() {
        return expired;
    }

}
