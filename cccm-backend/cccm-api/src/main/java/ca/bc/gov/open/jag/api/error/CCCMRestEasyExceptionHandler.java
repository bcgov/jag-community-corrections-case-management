package ca.bc.gov.open.jag.api.error;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class CCCMRestEasyExceptionHandler implements ExceptionMapper<ResteasyWebApplicationException> {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(CCCMRestEasyExceptionHandler.class));

    @Override
    public Response toResponse(ResteasyWebApplicationException e) {

        logger.error("Error during external request {} error {}", e.getResponse().getStatus(), e.getResponse().getEntity());

        CCCMErrorCode errorCode;

        switch (e.getResponse().getStatus()) {
            case 500:
                errorCode = CCCMErrorCode.UNKNOWN;
                break;
            case 400:
                errorCode = CCCMErrorCode.INVALIDREQUEST;
                break;
            case 404:
                errorCode = CCCMErrorCode.RECORDNOTFOUND;
                break;
            case 401:
                errorCode = CCCMErrorCode.INVALIDUSER;
                break;
            case 403:
                errorCode = CCCMErrorCode.INVALIDUSER;
                break;
            default:
                errorCode = CCCMErrorCode.UNKNOWN;
        }

        CCCMError cccmError = new CCCMError(errorCode, e.getMessage(), null);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(cccmError).build();

    }
}
