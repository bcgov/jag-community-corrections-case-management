package ca.bc.gov.open.jag.api.model.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.UpdateFormInput;

import java.math.BigDecimal;

public class UpdateForm {

    private UpdateFormInput updateFormInput;
    private BigDecimal locationId;
    private Boolean hasOverride;
    private String idirId;
    private Boolean complete;

    public UpdateForm(UpdateFormInput updateFormInput, BigDecimal locationId, Boolean hasOverride, String idirId, Boolean complete) {
        this.updateFormInput = updateFormInput;
        this.locationId = locationId;
        this.hasOverride = hasOverride;
        this.idirId = idirId;
        this.complete = complete;
    }

    public UpdateFormInput getUpdateFormInput() {
        return updateFormInput;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public Boolean getHasOverride() {
        return hasOverride;
    }

    public String getIdirId() {
        return idirId;
    }

    public Boolean getComplete() {
        return complete;
    }

}
