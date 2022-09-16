package ca.bc.gov.open.jag.api.user;

import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class GetUserIdTest {

    private static final String TEST = "TEST";

    @Inject
    UserApiImpl sut;

    @InjectMock
    UserDataService userDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "data-view")
    @DisplayName("200: should return user id")
    public void testGetUserDefaultLocationEndpoint() {

        Mockito.when(userDataService.getOracleId(any())).thenReturn(TEST);

        String result = sut.getUserId();

        Assertions.assertEquals(TEST, result);

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getUserIdTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getUserId());

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getGetUserExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getUserId());

    }

}
