package ca.bc.gov.open.jag.api.error;

public class CCCMException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private final CCCMErrorCode CCCMErrorCode;

    public CCCMException(String message, CCCMErrorCode CCCMErrorCode) {
        super(message);
        this.CCCMErrorCode = CCCMErrorCode;
    }

    public CCCMErrorCode getErrorCode() { return CCCMErrorCode; }

}
