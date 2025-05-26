package ca.bc.gov.open.jag.api.error;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class CCCMExceptionHandler implements ExceptionMapper<CCCMException> {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(CCCMExceptionHandler.class));

    @Override
    public Response toResponse(CCCMException e) {

        switch (e.getErrorCode()) {
            case RECORDNOTFOUND:
                logger.warn("Record not found exception code {} message {}", e.getErrorCode(), e.getMessage());
                return buildResponse(e.getErrorCode(), e.getMessage(), null, Response.Status.NOT_FOUND);
            case DATALOADERROR:
                logger.error("Data load exception code {} message {}", e.getErrorCode(), e.getMessage());
                return buildResponse(e.getErrorCode(), e.getMessage(),null, Response.Status.INTERNAL_SERVER_ERROR);
            case INVALIDUSER:
                logger.error("Invalid user exception code {} message {}", e.getErrorCode(), e.getMessage());
                return buildResponse(e.getErrorCode(), e.getMessage(),null, Response.Status.FORBIDDEN);
            case VALIDATIONERROR:
                logger.warn("Invalid request exception code {} message {}", e.getErrorCode(), e.getMessage());
                return buildResponse(e.getErrorCode(), e.getMessage(),null, Response.Status.BAD_REQUEST);
            case VALIDATIONERRORWITHRESULT:
                logger.warn("CRNA or SARA failed validation code {} message {}", e.getErrorCode(), e.getMessage());
                return buildResponse(e.getErrorCode(), e.getMessage(), e.getValidationResult(), Response.Status.BAD_REQUEST);
            case CLONEVALIDATIONERROR:
                logger.warn("Invalid clone request exception code {} message {}", e.getErrorCode(), e.getMessage());
                return buildResponse(e.getErrorCode(), e.getMessage(),null, Response.Status.BAD_REQUEST);
            default:
                logger.error(e.getMessage());
                return buildResponse(CCCMErrorCode.UNKNOWN, e.getMessage(), null, Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    private Response buildResponse(CCCMErrorCode errorCode, String errorMessage, ValidationResult validationResult, Response.Status status) {

        CCCMError cccmError = new CCCMError(errorCode, errorMessage, validationResult);

        return Response.status(status).entity(cccmError).build();

    }

}
