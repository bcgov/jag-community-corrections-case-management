package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.CompleteFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CreateFormInput;

import java.math.BigDecimal;

public interface ClientFormSaveService {

    BigDecimal createCRNA(CreateFormInput createFormInput, BigDecimal locationId);

    BigDecimal createSARA(CreateFormInput createFormInput, BigDecimal locationId);

    void updateForm(BigDecimal clientFormId, String updateFormInput);

    BigDecimal completeForm(CompleteFormInput completeFormInput, BigDecimal locationId);

    void deleteForm(BigDecimal clientFormId, String clientNum, BigDecimal locationId, String idirId);

}
