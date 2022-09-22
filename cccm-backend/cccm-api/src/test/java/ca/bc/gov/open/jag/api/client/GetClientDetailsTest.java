package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
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

@QuarkusTest
public class GetClientDetailsTest {

    private static final String CLIENT_NUM = "01";
    private static final String X_LOCATION_ID = "123";
    private static final String CURRENT_NAME = "TEST1, TESTER";
    private static final String GENDER = "M";
    private static final String CUSTODY_LOCATION = "TEST1";

    @Inject
    ClientsApiImpl sut;

    @InjectMock
    ClientDataService clientDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return client details")
    public void testGetClientDetailsEndpoint() {

        Mockito.when(clientDataService.clientDetails(Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(createClient());
        Client result = sut.getClientDetails(X_LOCATION_ID, CLIENT_NUM);

        Assertions.assertEquals(CURRENT_NAME, result.getCurrentName());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getClientDetails(null, CLIENT_NUM));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getClientDetails(null,CLIENT_NUM));

    }

    private Client createClient() {

        Client client = new Client();
        client.setClientNum(CLIENT_NUM);
        client.setCurrentName(CURRENT_NAME);
        client.setGender(GENDER);
        client.setCustodyLocation(CUSTODY_LOCATION);

        return client;

    }

}
