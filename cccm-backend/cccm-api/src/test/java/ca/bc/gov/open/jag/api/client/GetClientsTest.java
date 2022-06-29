package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

@QuarkusTest
public class GetClientsTest {

    @Inject
    ClientsApiImpl sut;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return clients")
    public void testGetClientsEndpoint() {

        List<Client> result = sut.searchClients(null, null,null,null,null,null,null,null);

        Assertions.assertEquals(1, result.size());

    }

    @Test

    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.searchClients(null,null,null,null,null,null,null,null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.searchClients(null,null,null,null,null,null,null,null));

    }

}
