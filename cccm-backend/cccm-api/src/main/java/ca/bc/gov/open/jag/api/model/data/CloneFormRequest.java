package ca.bc.gov.open.jag.api.model.data;

import java.math.BigDecimal;

public class CloneFormRequest {

    private String clientNumber;
    private BigDecimal clientFormId;
    private BigDecimal locationId;
    private Boolean hasOverride;

    public CloneFormRequest(String clientNumber, BigDecimal clientFormId, BigDecimal locationId, Boolean hasOverride) {
        this.clientNumber = clientNumber;
        this.clientFormId = clientFormId;
        this.locationId = locationId;
        this.hasOverride = hasOverride;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public BigDecimal getClientFormId() {
        return clientFormId;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public Boolean getHasOverride() {
        return hasOverride;
    }

}
