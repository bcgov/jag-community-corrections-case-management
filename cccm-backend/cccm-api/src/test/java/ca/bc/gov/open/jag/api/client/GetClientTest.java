package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class GetClientTest {

    @Inject
    ClientsApiImpl sut;

    @InjectMock
    ClientDataService clientDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return clients")
    public void testGetClientsEndpoint() {

        Mockito.when(clientDataService.clientProfile(Mockito.any())).thenReturn(createClient());
        Client result = sut.getClient("01");

        Assertions.assertEquals("01", result.getClientNum());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("404: client not found")
    public void testGetClientsNotFoundEndpoint() {

        Mockito.when(clientDataService.clientProfile(Mockito.any())).thenThrow(new CCCMException("Not found", CCCMErrorCode.RECORDNOTFOUND));

        Assertions.assertThrows(CCCMException.class, () -> sut.getClient("01"));

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getClient("01"));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getClient("01"));

    }

    private Client createClient() {

        Client client = new Client();
        client.setClientNum("01");
        client.setClientId(BigDecimal.ONE);
        client.setClientName("TEST1, TESTER");
        client.setGender("M");
        client.setBirthDate("1961-04-17");
        client.setCustodyLocation("TEST1");

        return client;

    }



}
