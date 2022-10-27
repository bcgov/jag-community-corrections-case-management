package ca.bc.gov.open.jag.api.model.service;

import java.math.BigDecimal;

public class DeleteRequest {

    private BigDecimal clientFormId;
    private BigDecimal locationId;
    private String clientNum;
    private String idirId;

    public DeleteRequest(BigDecimal clientFormId, BigDecimal locationId, String clientNum, String idirId) {
        this.clientFormId = clientFormId;
        this.locationId = locationId;
        this.clientNum = clientNum;
        this.idirId = idirId;
    }

    public BigDecimal getClientFormId() {
        return clientFormId;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public String getClientNum() {
        return clientNum;
    }

    public String getIdirId() {
        return idirId;
    }
}