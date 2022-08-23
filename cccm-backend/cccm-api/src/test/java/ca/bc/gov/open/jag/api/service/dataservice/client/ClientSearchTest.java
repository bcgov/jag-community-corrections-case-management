package ca.bc.gov.open.jag.api.service.dataservice.client;

import ca.bc.gov.open.jag.api.model.data.Address;
import ca.bc.gov.open.jag.api.model.data.Client;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@QuarkusTest
public class ClientSearchTest {

    @Inject
    ClientDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @InjectMock
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Test
    @DisplayName("Success: exact search should return clients")
    public void testExactGetClients() {

        Mockito.when(obridgeClientService.getClientSearch(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientList());
        Mockito.when(speedmentClientService.getClientAddress(Mockito.any())).thenReturn(createAddressList());

        List<ca.bc.gov.open.jag.cccm.api.openapi.model.Client> result = sut.clientSearch(new ClientSearch("TEST", null,null,1,1,1,null,null, null, null, null, null));

        Assertions.assertEquals(2, result.size());

    }

    @Test
    @DisplayName("Success: partial search should return clients")
    public void testPartialGetClients() {

        Mockito.when(obridgeClientService.getClientSearch(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientList());
        Mockito.when(speedmentClientService.getClientAddress(Mockito.any())).thenReturn(createAddressList());

        List<ca.bc.gov.open.jag.cccm.api.openapi.model.Client> result = sut.clientSearch(new ClientSearch("TEST%", null,null,null,null,null,null,null, null, null, null, null));

        Assertions.assertEquals(2, result.size());

    }

    @Test
    @DisplayName("Success: ID search should return clients")
    public void testIDGetClients() {

        Mockito.when(obridgeClientService.getClientSearch(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientList());
        Mockito.when(speedmentClientService.getClientAddress(Mockito.any())).thenReturn(createAddressList());

        List<ca.bc.gov.open.jag.cccm.api.openapi.model.Client> result = sut.clientSearch(new ClientSearch(null, null,null,null,null,null,null,null, null, "TEST", "TEST", null));

        Assertions.assertEquals(2, result.size());

    }

    @Test
    @DisplayName("Success: soundex search should return clients")
    public void testSoundexGetClients() {

        Mockito.when(obridgeClientService.getClientSearch(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientList());
        Mockito.when(speedmentClientService.getClientAddress(Mockito.any())).thenReturn(createAddressList());

        List<ca.bc.gov.open.jag.cccm.api.openapi.model.Client> result = sut.clientSearch(new ClientSearch("TEST", true,null,null,null,null,null,null, null, null, null, null));

        Assertions.assertEquals(2, result.size());

    }

    private List<Client> createClientList() {

        Client client1 = new Client();
        client1.setClientNo("01");
        client1.setClientName("TEST1, TESTER");
        client1.setCurrentNameYn("N");
        client1.setGenderCode("M");
        client1.setBirthDate("1961-04-17");
        client1.setCustodyLocation("TEST1");
        client1.setCommunityLocation("TEST1");

        Client client2 = new Client();
        client2.setClientNo("01");
        client2.setClientName("TEST2, TESTER");
        client2.setCurrentNameYn("N");
        client2.setGenderCode("F");
        client2.setBirthDate("1961-04-17");
        client2.setCustodyLocation("TEST2");
        client2.setCommunityLocation("TEST2");

        return Arrays.asList(client1, client2);

    }

    private List<Address> createAddressList() {

        Address address1 = new Address();
        address1.setAddressLine1Txt("1234 Street st");
        address1.setCityCd("TEST1");
        address1.setPostalCodeTxt("V0H 1V0");
        Address address2 = new Address();
        address2.setAddressLine1Txt("1235 Street st");
        address2.setCityCd("TEST2");
        address2.setPostalCodeTxt("V0H 2V0");

        return Arrays.asList(address1, address2);

    }



}
