package ca.bc.gov.open.jag.api.error;

public class CCCMError {

    public CCCMError(CCCMErrorCode error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    private final CCCMErrorCode error;
    private final String errorMessage;

    public CCCMErrorCode getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
