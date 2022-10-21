package ca.bc.gov.open.jag.api.model.data;

import java.math.BigDecimal;

public class CloneFormRequest {

    private String clientNumber;
    private BigDecimal clientFormId;
    private BigDecimal locationId;

    public CloneFormRequest(String clientNumber, BigDecimal clientFormId, BigDecimal locationId) {
        this.clientNumber = clientNumber;
        this.clientFormId = clientFormId;
        this.locationId = locationId;
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
}
