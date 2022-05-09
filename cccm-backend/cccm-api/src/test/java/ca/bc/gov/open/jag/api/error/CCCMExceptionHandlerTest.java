package ca.bc.gov.open.jag.api.error;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

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
    @DisplayName("Error: should return data load error")
    public void testUnknownError() {

        CCCMException error = new CCCMException(THIS_IS_A_TEST, CCCMErrorCode.UNKNOWN);

        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(500, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.UNKNOWN, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

}
