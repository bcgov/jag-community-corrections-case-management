package ca.bc.gov.open.jag.api.service.dataservice.client;

import ca.bc.gov.open.jag.api.model.service.ClientAddressSearch;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@QuarkusTest
public class ClientAddressSearchTest {

    @Inject
    ClientDataService sut;

    @InjectMock
    ObridgeClientService obridgeClientService;

    private static final LocalDate TEST_DATE = LocalDate.now();

    @Test
    @DisplayName("Success: exact search should return clients")
    public void testExactGetClients() {

        Mockito.when(obridgeClientService.getClientAddressSearch(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClientList());
        List<Client> result = sut.clientAddressSearch(new ClientAddressSearch("TEST", "TEST","TEST","TEST","TEST",true, true, "TEST@idir", BigDecimal.ONE));

        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals("01", result.get(0).getClientNum());
        Assertions.assertEquals("TEST1, TESTER", result.get(0).getClientName());
        Assertions.assertEquals("M", result.get(0).getGender());
        Assertions.assertEquals(TEST_DATE, result.get(0).getBirthDate());
        Assertions.assertEquals("TEST1", result.get(0).getCustodyLocation());
        Assertions.assertEquals("TEST", result.get(0).getCommunityInformation().getCaseManager());
        Assertions.assertEquals("TEST1", result.get(0).getCommunityInformation().getCommunityLocation());
        Assertions.assertEquals("TEST", result.get(0).getAddress().get(0).getFullAddress());

        Assertions.assertEquals("01", result.get(1).getClientNum());
        Assertions.assertEquals("TEST2, TESTER", result.get(1).getClientName());
        Assertions.assertEquals("F", result.get(1).getGender());
        Assertions.assertEquals(TEST_DATE, result.get(1).getBirthDate());
        Assertions.assertEquals("TEST2", result.get(1).getCustodyLocation());
        Assertions.assertEquals("TEST", result.get(1).getCommunityInformation().getCaseManager());
        Assertions.assertEquals("TEST2", result.get(1).getCommunityInformation().getCommunityLocation());
        Assertions.assertEquals("TEST", result.get(1).getAddress().get(0).getFullAddress());

    }

    private List<ca.bc.gov.open.jag.api.model.data.Client> createClientList() {

        ca.bc.gov.open.jag.api.model.data.Client client1 = new ca.bc.gov.open.jag.api.model.data.Client();
        client1.setClientNo("01");
        client1.setClientName("TEST1, TESTER");
        client1.setCurrentNameYn("N");
        client1.setGenderCode("M");
        client1.setBirthDate(TEST_DATE);
        client1.setCustodyLocation("TEST1");
        client1.setCommunityLocation("TEST1");
        client1.setAddress("TEST");
        client1.setCaseManager("TEST");

        ca.bc.gov.open.jag.api.model.data.Client client2 = new ca.bc.gov.open.jag.api.model.data.Client();
        client2.setClientNo("01");
        client2.setClientName("TEST2, TESTER");
        client2.setCurrentNameYn("N");
        client2.setGenderCode("F");
        client2.setBirthDate(TEST_DATE);
        client2.setCustodyLocation("TEST2");
        client2.setCommunityLocation("TEST2");
        client2.setAddress("TEST");
        client2.setCaseManager("TEST");

        return Arrays.asList(client1, client2);

    }

}
