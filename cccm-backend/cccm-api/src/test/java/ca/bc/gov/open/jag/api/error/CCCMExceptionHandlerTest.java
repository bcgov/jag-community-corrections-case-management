package ca.bc.gov.open.jag.api.error;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationError;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.Response;
import java.util.Collections;

@QuarkusTest
public class CCCMExceptionHandlerTest {

    private static final String THIS_IS_A_TEST = "This is a test";

    CCCMExceptionHandler sut = new CCCMExceptionHandler();

    @Test
    @DisplayName("Error: should return not found error")
    public void testNotFoundError() {

        CCCMException error = new CCCMException(THIS_IS_A_TEST, CCCMErrorCode.RECORDNOTFOUND);

        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(404, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.RECORDNOTFOUND, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

    @Test
    @DisplayName("Error: should return data load error")
    public void testDataLoadError() {

        CCCMException error = new CCCMException(THIS_IS_A_TEST, CCCMErrorCode.DATALOADERROR);

        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(500, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.DATALOADERROR, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

    @Test
    @DisplayName("Error: should return invalid error")
    public void testInvalidUserError() {

        CCCMException error = new CCCMException(THIS_IS_A_TEST, CCCMErrorCode.INVALIDUSER);

        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(403, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.INVALIDUSER, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

    @Test
    @DisplayName("Error: should return invalid request error")
    public void testInvalidRequestError() {

        CCCMException error = new CCCMException(THIS_IS_A_TEST, CCCMErrorCode.VALIDATIONERROR);

        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(400, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.VALIDATIONERROR, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

    @Test
    @DisplayName("Error: should return invalid request error")
    public void testCloneRequestError() {

        CCCMException error = new CCCMException(THIS_IS_A_TEST, CCCMErrorCode.CLONEVALIDATIONERROR);

        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(400, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.CLONEVALIDATIONERROR, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

    @Test
    @DisplayName("Error: should return data load error")
    public void testUnknownError() {

        CCCMException error = new CCCMException(THIS_IS_A_TEST, CCCMErrorCode.UNKNOWN);

        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(500, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.UNKNOWN, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

    @Test
    @DisplayName("Error: should return validation result error")
    public void testValidationResultError() {

        ValidationResult validationResult = new ValidationResult();
        ValidationError validationError = new ValidationError();
        validationError.setMessage("TEST");
        validationError.setAnswerKey("KEY");
        validationResult.setErrors(Collections.singletonList(validationError));

        CCCMException error = new CCCMException(THIS_IS_A_TEST, CCCMErrorCode.VALIDATIONERRORWITHRESULT, validationResult);

        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(400, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.VALIDATIONERRORWITHRESULT, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());
        Assertions.assertEquals(1, body.getValidationResult().getErrors().size());
        Assertions.assertEquals("KEY", body.getValidationResult().getErrors().get(0).getAnswerKey());
        Assertions.assertEquals("TEST", body.getValidationResult().getErrors().get(0).getMessage());

    }

}
