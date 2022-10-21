package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.CompleteFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CreateFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.UpdateFormInput;

import java.math.BigDecimal;

public interface ClientFormSaveService {

    BigDecimal createCRNA(CreateFormInput createFormInput, BigDecimal locationId);

    BigDecimal createSARA(CreateFormInput createFormInput, BigDecimal locationId);

    BigDecimal updateForm(UpdateFormInput updateFormInput, BigDecimal locationId);

    BigDecimal completeForm(CompleteFormInput completeFormInput, BigDecimal locationId);

}
