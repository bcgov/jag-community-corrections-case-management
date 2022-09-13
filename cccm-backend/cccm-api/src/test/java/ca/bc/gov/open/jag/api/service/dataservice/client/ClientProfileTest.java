package ca.bc.gov.open.jag.api.service.dataservice.client;

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
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static ca.bc.gov.open.jag.api.util.MappingUtils.calculateAge;

@QuarkusTest
public class ClientProfileTest {

    private static final String TEST_NAME = "TEST1, TESTER";
    private static final String CURRENT_NAME_YN = "N";
    private static final String GENDER_CODE = "M";
    private static final String BIRTH_DATE = "1961-04-17";
    private static final String CUSTODY_LOCATION = "Custody Location";
    private static final String COMMUNITY_LOCATION = "Community Location";
    private static final String ADDRESS = "Address";
    private static final String CASE_MANAGER = "Case Manager";
    @Inject
    ClientDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return clients")
    public void testGetClients() {

        Map map = new HashMap();
        map.put("locationId", Double.valueOf(1));

        Mockito.when(obridgeClientService.getProfileById(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientProfile());
        Mockito.when(obridgeClientService.getLocation()).thenReturn(map);

        Client result = sut.clientProfile("01", "test@idir");

        Assertions.assertEquals("01", result.getClientNum());
        Assertions.assertEquals(TEST_NAME, result.getClientName());
        Assertions.assertEquals(calculateAge(BIRTH_DATE), result.getClientAge());
        Assertions.assertEquals(GENDER_CODE, result.getGender());
        Assertions.assertEquals(BIRTH_DATE, result.getBirthDate());
        Assertions.assertEquals(CUSTODY_LOCATION, result.getCustodyLocation());
        Assertions.assertEquals(CUSTODY_LOCATION, result.getGeneralInformation().getInstitution());
        Assertions.assertEquals(COMMUNITY_LOCATION, result.getCommunityInformation().getCommunityLocation());
        Assertions.assertEquals(ADDRESS, result.getAddress().get(0).getFullAddress());
        Assertions.assertEquals(CASE_MANAGER, result.getCommunityInformation().getCaseManager());

    }


    private ca.bc.gov.open.jag.api.model.data.Client createClient() {

        ca.bc.gov.open.jag.api.model.data.Client client1 = new ca.bc.gov.open.jag.api.model.data.Client();
        client1.setClientNo("01");
        client1.setClientName(TEST_NAME);
        client1.setCurrentNameYn(CURRENT_NAME_YN);
        client1.setGenderCode(GENDER_CODE);
        client1.setBirthDate(BIRTH_DATE);
        client1.setCustodyLocation(CUSTODY_LOCATION);
        client1.setCommunityLocation(COMMUNITY_LOCATION);
        client1.setAddress(ADDRESS);
        client1.setCaseManager(CASE_MANAGER);

        return client1;

    }

    private ClientProfile createClientProfile() {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setClient(createClient());
        return clientProfile;
    }

}
