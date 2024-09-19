package ca.bc.gov.open.jag.api.client;


import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
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

import java.time.LocalDate;

@QuarkusTest
public class GetClientTest {

    private static final String CLIENT_NUM = "01";
    private static final String X_LOCATION_ID = "123";
    private static final LocalDate BIRTH_DATE = LocalDate.now();

    @Inject
    ClientsApiImpl sut;

    @InjectMock
    ClientDataService clientDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return clients")
    public void testGetClientsEndpoint() {

        Mockito.when(clientDataService.clientProfile(Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(createClient());
        Client result = sut.getClient(X_LOCATION_ID, CLIENT_NUM);

        Assertions.assertEquals(CLIENT_NUM, result.getClientNum());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getClient(null, CLIENT_NUM));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getClient(null,CLIENT_NUM));

    }

    private Client createClient() {

        Client client = new Client();
        client.setClientNum("01");
        client.setClientName("TEST1, TESTER");
        client.setGender("M");
        client.setBirthDate(BIRTH_DATE);
        client.setCustodyLocation("TEST1");

        return client;

    }

}
