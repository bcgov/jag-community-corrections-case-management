package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.data.CloneFormRequest;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CreateFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.UpdateFormInput;

import java.math.BigDecimal;

public interface ClientFormSaveService {

    BigDecimal createCRNA(CreateFormInput createFormInput, BigDecimal locationId);

    BigDecimal createSARA(CreateFormInput createFormInput, BigDecimal locationId);

    void updateForm(BigDecimal clientFormId, String updateFormInput);

    BigDecimal completeForm(UpdateFormInput completeFormInput, BigDecimal locationId);

    void editForm(UpdateFormInput updateFormInput, BigDecimal locationId);

    void linkForm(UpdateFormInput linkFormInput, BigDecimal locationId);

    void deleteForm(BigDecimal clientFormId, String clientNum, BigDecimal locationId, String idirId);

    BigDecimal cloneClientForm(CloneFormRequest cloneFormRequest, String idirId);

}
