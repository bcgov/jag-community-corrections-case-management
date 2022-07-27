package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.eclipse.microprofile.rest.client.inject.RestClient;
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
    @RestClient
    ObridgeClientService obridgeClientService;

    @InjectMock
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return clients")
    public void testGetClientsEndpoint() {

        Mockito.when(obridgeClientService.getClientById(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientList());
        Mockito.when(speedmentClientService.getClientAddress(Mockito.any())).thenReturn(createAddressList());

        Client result = sut.getClient(BigDecimal.ONE);

        Assertions.assertEquals("123", result.getClientNum());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("404: client not found")
    public void testGetClientsNotFoundEndpoint() {

        Mockito.when(obridgeClientService.getClientById(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Collections.emptyList());

        Assertions.assertThrows(CCCMException.class, () -> sut.getClient(BigDecimal.ONE));

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getClient(BigDecimal.ONE));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getClient(BigDecimal.ONE));

    }

    private List<ca.bc.gov.open.jag.api.model.Client> createClientList() {

        ca.bc.gov.open.jag.api.model.Client client1 = new ca.bc.gov.open.jag.api.model.Client();
        client1.setClientNo(BigDecimal.valueOf(123));
        client1.setClientName("TEST1, TESTER");
        client1.setCurrentNameYn("N");
        client1.setGenderCode("M");
        client1.setBirthDate("1961-04-17");
        client1.setCustodyLocation("TEST1");
        client1.setCommunityLocation("TEST1");


        return Collections.singletonList(client1);

    }

    private List<ca.bc.gov.open.jag.api.model.Address> createAddressList() {

        ca.bc.gov.open.jag.api.model.Address address1 = new ca.bc.gov.open.jag.api.model.Address();
        address1.setAddressLine1Txt("1234 Street st");
        address1.setCityCd("TEST1");
        address1.setPostalCodeTxt("V0H 1V0");
        ca.bc.gov.open.jag.api.model.Address address2 = new ca.bc.gov.open.jag.api.model.Address();
        address2.setAddressLine1Txt("1235 Street st");
        address2.setCityCd("TEST2");
        address2.setPostalCodeTxt("V0H 2V0");

        return Arrays.asList(address1, address2);

    }

}
