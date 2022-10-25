package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.validation.ValidationResult;

public interface ValidationService {

    ValidationResult validateCRNA(String answers);

    ValidationResult validateSARA(String answers);

}
