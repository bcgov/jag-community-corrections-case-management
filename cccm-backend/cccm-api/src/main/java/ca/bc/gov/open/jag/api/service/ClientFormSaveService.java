package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.data.CloneFormRequest;
import ca.bc.gov.open.jag.api.model.service.UpdateForm;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CreateFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LinkFormInput;

import java.math.BigDecimal;

public interface ClientFormSaveService {

    BigDecimal createCRNA(CreateFormInput createFormInput, BigDecimal locationId);

    BigDecimal createSARA(CreateFormInput createFormInput, BigDecimal locationId);

    BigDecimal createACUTE(CreateFormInput createFormInput, BigDecimal locationId);

    BigDecimal createStatic99r(CreateFormInput createFormInput, BigDecimal locationId);

    BigDecimal createStable(CreateFormInput createFormInput, BigDecimal locationId);

    BigDecimal createOverall(CreateFormInput createFormInput, BigDecimal locationId);

    void updateForm(BigDecimal clientFormId, String updateFormInput);

    void editForm(UpdateForm updateForm);

    void linkForm(LinkFormInput linkFormInput, BigDecimal locationId);

    void deleteForm(BigDecimal clientFormId, String clientNum, BigDecimal locationId, String idirId, Boolean hasOverride);

    BigDecimal cloneClientForm(CloneFormRequest cloneFormRequest, String idirId);

}
