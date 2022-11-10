package ca.bc.gov.open.jag.api.error;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class CCCMExceptionHandler implements ExceptionMapper<CCCMException> {

    private static final Logger logger = Logger.getLogger(String.valueOf(CCCMExceptionHandler.class));

    @Override
    public Response toResponse(CCCMException e) {

        switch (e.getErrorCode()) {
            case RECORDNOTFOUND:
                logger.severe("Record not found exception");
                return buildResponse(e.getErrorCode(), e.getMessage(), null, Response.Status.NOT_FOUND);
            case DATALOADERROR:
                logger.severe("Data load exception ");
                return buildResponse(e.getErrorCode(), e.getMessage(),null, Response.Status.INTERNAL_SERVER_ERROR);
            case INVALIDUSER:
                logger.severe("Invalid user exception ");
                return buildResponse(e.getErrorCode(), e.getMessage(),null, Response.Status.FORBIDDEN);
            case VALIDATIONERROR:
                logger.severe("Invalid request exception ");
                return buildResponse(e.getErrorCode(), e.getMessage(),null, Response.Status.BAD_REQUEST);
            case VALIDATIONERRORWITHRESULT:
                logger.severe("CRNA or SARA failed validation ");
                return buildResponse(e.getErrorCode(), e.getMessage(), e.getValidationResult(), Response.Status.BAD_REQUEST);
            case CLONEVALIDATIONERROR:
                logger.severe("Invalid clone request exception ");
                return buildResponse(e.getErrorCode(), e.getMessage(),null, Response.Status.BAD_REQUEST);
            default:
                logger.severe(e.getMessage());
                return buildResponse(CCCMErrorCode.UNKNOWN, e.getMessage(), null, Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    private Response buildResponse(CCCMErrorCode errorCode, String errorMessage, ValidationResult validationResult, Response.Status status) {

        CCCMError cccmError = new CCCMError(errorCode, errorMessage, validationResult);

        return Response.status(status).entity(cccmError).build();

    }

}
