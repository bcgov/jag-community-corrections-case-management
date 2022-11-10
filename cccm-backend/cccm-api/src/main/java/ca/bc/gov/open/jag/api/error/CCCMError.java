package ca.bc.gov.open.jag.api.error;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;

public class CCCMError {

    public CCCMError(CCCMErrorCode error, String errorMessage, ValidationResult validationResult) {
        this.error = error;
        this.errorMessage = errorMessage;
        this.validationResult = validationResult;
    }

    private final CCCMErrorCode error;
    private final String errorMessage;
    private final ValidationResult validationResult;

    public CCCMErrorCode getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}
