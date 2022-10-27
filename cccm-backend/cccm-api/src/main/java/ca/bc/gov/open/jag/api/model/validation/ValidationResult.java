package ca.bc.gov.open.jag.api.model.validation;

import java.util.List;

public class ValidationResult {

    private List<ValidationError> validationErrors;

    public ValidationResult(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

}
