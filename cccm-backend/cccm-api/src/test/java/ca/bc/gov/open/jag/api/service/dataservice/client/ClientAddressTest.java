package ca.bc.gov.open.jag.api.service.dataservice.client;

import ca.bc.gov.open.jag.api.model.data.Address;
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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class ClientAddressTest {

    private static final String ADDRESS = "Address";
    private static final String CLIENT_NUM = "01";
    private static final String TEST_IDIR = "test@idir";
    private static final String LOCATION = "123";

    @Inject
    ClientDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return addresses")
    public void testGetAddress() {

        Mockito.when(obridgeClientService.getAddressById(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Collections.singletonList(createAddress()));

        List<ca.bc.gov.open.jag.cccm.api.openapi.model.Address> result = sut.clientAddress(CLIENT_NUM, TEST_IDIR, LOCATION);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(ADDRESS, result.get(0).getFullAddress());
        Assertions.assertFalse(result.get(0).getExpired());

    }


    private ca.bc.gov.open.jag.api.model.data.Address createAddress() {

        LocalDate testDate = LocalDate.now();
        testDate = testDate.minusYears(1);

        Address address = new Address();
        address.setFullAddress(ADDRESS);
        address.setExpiryDate(testDate.toString());

        return address;

    }

}
