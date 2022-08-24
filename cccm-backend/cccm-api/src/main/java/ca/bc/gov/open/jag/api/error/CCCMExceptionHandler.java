package ca.bc.gov.open.jag.api.error;

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
                return buildResponse(e.getErrorCode(), e.getMessage(), Response.Status.NOT_FOUND);
            case DATALOADERROR:
                logger.severe("Data load exception ");
                return buildResponse(e.getErrorCode(), e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
            case INVALIDUSER:
                logger.severe("Invalid user exception ");
                return buildResponse(e.getErrorCode(), e.getMessage(), Response.Status.FORBIDDEN);
            case VALIDATIONERROR:
                logger.severe("Invalid request exception ");
                return buildResponse(e.getErrorCode(), e.getMessage(), Response.Status.BAD_REQUEST);
            default:
                logger.severe(e.getMessage());
                return buildResponse(CCCMErrorCode.UNKNOWN, e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    private Response buildResponse(CCCMErrorCode errorCode, String errorMessage, Response.Status status) {

        CCCMError cccmError = new CCCMError(errorCode, errorMessage);

        return Response.status(status).entity(cccmError).build();

    }

}
