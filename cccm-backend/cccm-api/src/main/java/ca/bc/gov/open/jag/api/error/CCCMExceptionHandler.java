package ca.bc.gov.open.jag.api.error;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CCCMExceptionHandler implements ExceptionMapper<CCCMException> {

    @Override
    public Response toResponse(CCCMException e) {

        switch (e.getErrorCode()) {
            case RECORDNOTFOUND:
                return buildResponse(e.getErrorCode(), e.getMessage(), Response.Status.NOT_FOUND);
            case DATALOADERROR:
                return buildResponse(e.getErrorCode(), e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
            default:
                return buildResponse(CCCMErrorCode.UNKNOWN, e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    private Response buildResponse(CCCMErrorCode errorCode, String errorMessage, Response.Status status) {

        CCCMError cccmError = new CCCMError(errorCode, errorMessage);

        return Response.status(status).entity(cccmError).build();

    }

}
