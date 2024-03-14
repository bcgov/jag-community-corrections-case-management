package ca.bc.gov.open.jag.api.user;

import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class GetUserDefaultLocationTest {

    private static final String TEST_CD = "CD";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    UserApiImpl sut;

    @InjectMock
    UserDataService userDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "data-view")
    @DisplayName("200: should return form types")
    public void testGetUserDefaultLocationEndpoint() {

        Code location = new Code();
        location.setKey(TEST_CD);
        location.setValue(TEST_VALUE);

        Mockito.when(userDataService.getDefaultLocation(any())).thenReturn(location);

        Code result = sut.getUserDefaultLocation();

        Assertions.assertEquals(TEST_CD, result.getKey());
        Assertions.assertEquals(TEST_VALUE, result.getValue());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getDefaultLocationsTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getUserDefaultLocation());

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getDefaultLocationTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getUserDefaultLocation());

    }

}
