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
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@QuarkusTest
@Transactional
public class GetClientsTest {

    @Inject
    ClientsApiImpl sut;

    @InjectMock
    ClientDataService clientDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return clients")
    public void testGetClientsEndpoint() {

        Mockito.when(clientDataService.clientSearch(Mockito.any())).thenReturn(createClientList());

        List<ca.bc.gov.open.jag.cccm.api.openapi.model.Client> result = sut.searchClients(null,null, null,null,null,null,null,null, null, null, null);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("01", result.get(0).getClientNum());
        Assertions.assertEquals("TEST1, TESTER", result.get(0).getClientName());
        Assertions.assertEquals("M", result.get(0).getGender());
        Assertions.assertEquals("1961-04-17", result.get(0).getBirthDate());
        Assertions.assertEquals("TEST1", result.get(0).getCustodyLocation());

        Assertions.assertEquals("01", result.get(1).getClientNum());
        Assertions.assertEquals("TEST2, TESTER", result.get(1).getClientName());
        Assertions.assertEquals("F", result.get(1).getGender());
        Assertions.assertEquals("1961-04-17", result.get(1).getBirthDate());
        Assertions.assertEquals("TEST2", result.get(1).getCustodyLocation());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.searchClients(null,null,null,null,null,null,null,null, null,null, null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.searchClients(null,null,null,null,null,null,null,null, null, null, null));

    }

    private List<Client> createClientList() {

        Client client1 = new Client();
        client1.setClientNum("01");
        client1.setClientName("TEST1, TESTER");
        client1.setGender("M");
        client1.setBirthDate("1961-04-17");
        client1.setCustodyLocation("TEST1");

        Client client2 = new Client();
        client2.setClientNum("01");
        client2.setClientName("TEST2, TESTER");
        client2.setGender("F");
        client2.setBirthDate("1961-04-17");
        client2.setCustodyLocation("TEST2");

        return Arrays.asList(client1, client2);

    }

}
