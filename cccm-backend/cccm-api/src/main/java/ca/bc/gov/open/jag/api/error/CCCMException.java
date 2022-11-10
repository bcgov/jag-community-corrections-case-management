package ca.bc.gov.open.jag.api.error;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;

public class CCCMException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private final CCCMErrorCode CCCMErrorCode;
    private ValidationResult validationResult;

    public CCCMException(String message, CCCMErrorCode CCCMErrorCode) {
        super(message);
        this.CCCMErrorCode = CCCMErrorCode;
    }

    public CCCMException(String message, CCCMErrorCode CCCMErrorCode, ValidationResult validationResult) {
        super(message);
        this.validationResult = validationResult;
        this.CCCMErrorCode = CCCMErrorCode;
    }

    public CCCMErrorCode getErrorCode() { return CCCMErrorCode; }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

}
