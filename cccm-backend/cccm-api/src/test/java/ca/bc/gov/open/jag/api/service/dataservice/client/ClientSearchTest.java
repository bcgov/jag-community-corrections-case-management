package ca.bc.gov.open.jag.api.service.dataservice.client;

import ca.bc.gov.open.jag.api.model.data.Client;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
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

    private static final String TEST_STRING = "TEST";
    private static final String CLIENT_NO = "01";
    private static final String TEST_1_TESTER = "TEST1, TESTER";
    private static final String TEST_2_TESTER = "TEST2, TESTER";
    private static final String CURRENT_NAME_YN = "N";
    private static final String GENDER_M = "M";
    private static final String GENDER_F = "F";
    private static final String BIRTH_DATE = "1961-04-17";
    private static final String TEST_1 = "TEST1";
    private static final String TEST_2 = "TEST2";
    @Inject
    ClientDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: exact search should return clients")
    public void testExactGetClients() {

        Mockito.when(obridgeClientService.getClientSearch(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientList());
        List<ca.bc.gov.open.jag.cccm.api.openapi.model.Client> result = sut.clientSearch(new ClientSearch(TEST_STRING, null,null,1,1,1,true,null, null, null, "TEST@idir", null));

        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals(CLIENT_NO, result.get(0).getClientNum());
        Assertions.assertEquals(TEST_1_TESTER, result.get(0).getClientName());
        Assertions.assertEquals(CURRENT_NAME_YN, result.get(0).getCurrentNameYn());
        Assertions.assertEquals(GENDER_M, result.get(0).getGender());
        Assertions.assertEquals(BIRTH_DATE, result.get(0).getBirthDate());
        Assertions.assertEquals(TEST_1, result.get(0).getGeneralInformation().getInstitution());
        Assertions.assertEquals(TEST_1, result.get(0).getCommunityInformation().getCommunityLocation());
        Assertions.assertEquals(TEST_STRING, result.get(0).getAddress().get(0).getFullAddress());
        Assertions.assertFalse(result.get(0).getAddress().get(0).getExpired());
        Assertions.assertTrue(result.get(0).getAddress().get(0).getPrimary());
        Assertions.assertEquals(TEST_STRING, result.get(0).getCommunityInformation().getCaseManager());

        Assertions.assertEquals(CLIENT_NO, result.get(1).getClientNum());
        Assertions.assertEquals(TEST_2_TESTER, result.get(1).getClientName());
        Assertions.assertEquals(CURRENT_NAME_YN, result.get(1).getCurrentNameYn());
        Assertions.assertEquals(GENDER_F, result.get(1).getGender());
        Assertions.assertEquals(BIRTH_DATE, result.get(1).getBirthDate());
        Assertions.assertEquals(TEST_2, result.get(1).getGeneralInformation().getInstitution());
        Assertions.assertEquals(TEST_2, result.get(1).getCommunityInformation().getCommunityLocation());
        Assertions.assertEquals(TEST_STRING, result.get(1).getAddress().get(0).getFullAddress());
        Assertions.assertFalse(result.get(1).getAddress().get(0).getExpired());
        Assertions.assertTrue(result.get(1).getAddress().get(0).getPrimary());
        Assertions.assertEquals(TEST_STRING, result.get(1).getCommunityInformation().getCaseManager());

    }

    @Test
    @DisplayName("Success: partial search should return clients")
    public void testPartialGetClients() {

        Mockito.when(obridgeClientService.getClientSearch(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientList());
        List<ca.bc.gov.open.jag.cccm.api.openapi.model.Client> result = sut.clientSearch(new ClientSearch("TEST%", null,null,null,null,null,true,null, null, null, "TEST@idir", null));

        Assertions.assertEquals(2, result.size());

    }

    @Test
    @DisplayName("Success: ID search should return clients")
    public void testIDGetClients() {

        Mockito.when(obridgeClientService.getClientSearch(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientList());

        List<ca.bc.gov.open.jag.cccm.api.openapi.model.Client> result = sut.clientSearch(new ClientSearch( null,null,null,null,null,null,true, null, TEST_STRING, TEST_STRING, "TEST@idir", null));

        Assertions.assertEquals(2, result.size());

    }

    @Test
    @DisplayName("Success: soundex search should return clients")
    public void testSoundexGetClients() {

        Mockito.when(obridgeClientService.getClientSearch(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientList());

        List<ca.bc.gov.open.jag.cccm.api.openapi.model.Client> result = sut.clientSearch(new ClientSearch(TEST_STRING, true,null,null,null,null,null,null, null, null, "TEST@idir", null));

        Assertions.assertEquals(2, result.size());

    }

    private List<Client> createClientList() {

        Client client1 = new Client();
        client1.setClientNo(CLIENT_NO);
        client1.setClientName(TEST_1_TESTER);
        client1.setCurrentNameYn(CURRENT_NAME_YN);
        client1.setGenderCode(GENDER_M);
        client1.setBirthDate(BIRTH_DATE);
        client1.setCustodyLocation(TEST_1);
        client1.setCommunityLocation(TEST_1);
        client1.setAddress(TEST_STRING);
        client1.setCaseManager(TEST_STRING);

        Client client2 = new Client();
        client2.setClientNo(CLIENT_NO);
        client2.setClientName(TEST_2_TESTER);
        client2.setCurrentNameYn(CURRENT_NAME_YN);
        client2.setGenderCode(GENDER_F);
        client2.setBirthDate(BIRTH_DATE);
        client2.setCustodyLocation(TEST_2);
        client2.setCommunityLocation(TEST_2);
        client2.setAddress(TEST_STRING);
        client2.setCaseManager(TEST_STRING);

        return Arrays.asList(client1, client2);

    }

}
