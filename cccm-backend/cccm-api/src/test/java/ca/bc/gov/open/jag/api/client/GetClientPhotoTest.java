package ca.bc.gov.open.jag.api.client;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class GetClientPhotoTest {

    @Inject
    ClientsApiImpl sut;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return null photo")
    public void testGetClientPhotoEndpoint() {
        sut.getClientPhoto(null);

        Assertions.assertNull(sut.getClientPhoto(null));

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getClientPhoto(null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getClientPhoto(null));

    }

}
