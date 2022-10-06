package ca.bc.gov.open.jag.api.model.service;

import java.math.BigDecimal;

public class ClientAddressSearch {

    private String addressType;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private Boolean expired;
    private Boolean location;
    private String user;
    private BigDecimal locationId;

    public ClientAddressSearch(String addressType, String address, String city, String province, String postalCode, Boolean expired, Boolean location, String user, BigDecimal locationId) {
        this.addressType = addressType;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.expired = expired;
        this.location = location;
        this.user = user;
        this.locationId = locationId;
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

    public Boolean getLocation() {
        return location;
    }

    public String getUser() {
        return user;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }
}
