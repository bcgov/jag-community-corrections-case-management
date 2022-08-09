package ca.bc.gov.open.jag.api.service.dataservice.client;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.Address;
import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class ClientProfileTest {

    @Inject
    ClientDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @InjectMock
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Test
    @DisplayName("Success: should return clients")
    public void testGetClients() {

        Mockito.when(obridgeClientService.getClientById(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientList());
        Mockito.when(obridgeClientService.getProfileById(Mockito.any())).thenReturn(new ClientProfile());
        Mockito.when(speedmentClientService.getClientAddress(Mockito.any())).thenReturn(createAddressList());
        Mockito.when(speedmentClientService.getAlerts(Mockito.any())).thenReturn(Collections.emptyList());

        Client result = sut.clientProfile("01");

        Assertions.assertEquals("01", result.getClientNum());

    }

    @Test
    @DisplayName("Exception: client not found")
    public void testGetClientsNotFound() {

        Mockito.when(obridgeClientService.getClientById(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Collections.emptyList());

        Assertions.assertThrows(CCCMException.class, () -> sut.clientProfile("01"));

    }

    private List<ca.bc.gov.open.jag.api.model.data.Client> createClientList() {

        ca.bc.gov.open.jag.api.model.data.Client client1 = new ca.bc.gov.open.jag.api.model.data.Client();
        client1.setClientNo("01");
        client1.setClientName("TEST1, TESTER");
        client1.setCurrentNameYn("N");
        client1.setGenderCode("M");
        client1.setBirthDate("1961-04-17");
        client1.setCustodyLocation("TEST1");
        client1.setCommunityLocation("TEST1");


        return Collections.singletonList(client1);

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
