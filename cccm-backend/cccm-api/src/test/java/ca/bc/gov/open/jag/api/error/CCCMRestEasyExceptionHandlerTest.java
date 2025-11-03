package ca.bc.gov.open.jag.api.error;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class CCCMRestEasyExceptionHandlerTest {

    private static final String THIS_IS_A_TEST = "This is a test";

    CCCMRestEasyExceptionHandler sut = new CCCMRestEasyExceptionHandler();

    @Test
    @DisplayName("Error: should return not found error")
    public void testNotFoundError() {

        Response testResponse = Response.status(404).entity(THIS_IS_A_TEST).build();

        ClientErrorException error = Mockito.mock(ClientErrorException.class);
        Mockito.when(error.getResponse()).thenReturn(testResponse);
        Mockito.when(error.getMessage()).thenReturn(THIS_IS_A_TEST);
        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(500, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.RECORDNOTFOUND, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

    @Test
    @DisplayName("Error: should return data load error")
    public void testDataLoadError() {

        Response testResponse = Response.status(500).entity(THIS_IS_A_TEST).build();

        ClientErrorException error = Mockito.mock(ClientErrorException.class);
        Mockito.when(error.getResponse()).thenReturn(testResponse);
        Mockito.when(error.getMessage()).thenReturn(THIS_IS_A_TEST);
        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(500, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.UNKNOWN, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

    @Test
    @DisplayName("Error: should return invalid error")
    public void testInvalidUserError() {

        Response testResponse = Response.status(403).entity(THIS_IS_A_TEST).build();

        ClientErrorException error = Mockito.mock(ClientErrorException.class);
        Mockito.when(error.getResponse()).thenReturn(testResponse);
        Mockito.when(error.getMessage()).thenReturn(THIS_IS_A_TEST);
        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(500, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.INVALIDUSER, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

    @Test
    @DisplayName("Error: should return invalid error")
    public void testUnAuthInvalidUserError() {

        Response testResponse = Response.status(401).entity(THIS_IS_A_TEST).build();

        ClientErrorException error = Mockito.mock(ClientErrorException.class);
        Mockito.when(error.getResponse()).thenReturn(testResponse);
        Mockito.when(error.getMessage()).thenReturn(THIS_IS_A_TEST);
        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(500, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.INVALIDUSER, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

    @Test
    @DisplayName("Error: should return invalid request error")
    public void testInvalidRequestError() {

        Response testResponse = Response.status(400).entity(THIS_IS_A_TEST).build();

        ClientErrorException error = Mockito.mock(ClientErrorException.class);
        Mockito.when(error.getResponse()).thenReturn(testResponse);
        Mockito.when(error.getMessage()).thenReturn(THIS_IS_A_TEST);
        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(500, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.INVALIDREQUEST, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

    @Test
    @DisplayName("Error: should return data load error")
    public void testUnknownError() {

        Response testResponse = Response.status(418).entity(THIS_IS_A_TEST).build();

        ClientErrorException error = Mockito.mock(ClientErrorException.class);
        Mockito.when(error.getResponse()).thenReturn(testResponse);
        Mockito.when(error.getMessage()).thenReturn(THIS_IS_A_TEST);
        Response result = sut.toResponse(error);

        CCCMError body = (CCCMError)result.getEntity();

        Assertions.assertEquals(500, result.getStatus());
        Assertions.assertEquals(CCCMErrorCode.UNKNOWN, body.getError());
        Assertions.assertEquals(THIS_IS_A_TEST, body.getErrorMessage());

    }

}
