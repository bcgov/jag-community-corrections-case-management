package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@QuarkusTest
public class GetClientsTest {

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

        Mockito.when(obridgeClientService.getClientSearch(Mockito.any(), Mockito.any())).thenReturn(createClientList());
        Mockito.when(speedmentClientService.getClientAddress(Mockito.any())).thenReturn(createAddressList());

       List<ca.bc.gov.open.jag.cccm.api.openapi.model.Client> result = sut.searchClients(null, null,null,null,null,null,null,null);

       Assertions.assertEquals(2, result.size());

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

    private List<ca.bc.gov.open.jag.api.model.Client> createClientList() {

        ca.bc.gov.open.jag.api.model.Client client1 = new ca.bc.gov.open.jag.api.model.Client();
        client1.setClientNo(BigDecimal.valueOf(123));
        client1.setClientName("TEST1, TESTER");
        client1.setCurrentNameYn("N");
        client1.setGenderCode("M");
        client1.setBirthDate("1961-04-17");
        client1.setCustodyLocation("TEST1");
        client1.setCommunityLocation("TEST1");

        ca.bc.gov.open.jag.api.model.Client client2 = new ca.bc.gov.open.jag.api.model.Client();
        client2.setClientNo(BigDecimal.valueOf(124));
        client2.setClientName("TEST2, TESTER");
        client2.setCurrentNameYn("N");
        client2.setGenderCode("F");
        client2.setBirthDate("1961-04-17");
        client2.setCustodyLocation("TEST2");
        client2.setCommunityLocation("TEST2");

        return Arrays.asList(client1, client2);

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
