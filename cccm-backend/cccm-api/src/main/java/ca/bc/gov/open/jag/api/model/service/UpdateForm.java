package ca.bc.gov.open.jag.api.model.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.InterventionsChecked;
import ca.bc.gov.open.jag.cccm.api.openapi.model.UpdateFormInput;

import java.math.BigDecimal;
import java.util.List;

public class UpdateForm {

    private UpdateFormInput updateFormInput;
    private BigDecimal locationId;
    private Boolean hasOverride;
    private String idirId;
    private Boolean complete;
    private List<InterventionsChecked> interventionKeys;

    public UpdateForm(UpdateFormInput updateFormInput, BigDecimal locationId, Boolean hasOverride, String idirId, Boolean complete, List<InterventionsChecked> interventionKeys) {
        this.updateFormInput = updateFormInput;
        this.locationId = locationId;
        this.hasOverride = hasOverride;
        this.idirId = idirId;
        this.complete = complete;
        this.interventionKeys = interventionKeys;
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

    public List<InterventionsChecked> getInterventionKeys() {
        return interventionKeys;
    }
}
