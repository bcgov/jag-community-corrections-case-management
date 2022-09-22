package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class GetClientAddressTest {

    private static final String CLIENT_NUM = "01";
    private static final String X_LOCATION_ID = "123";

    @Inject
    ClientsApiImpl sut;

    @InjectMock
    ClientDataService clientDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return address list")
    public void testGetClientAddressEndpoint() {

        Mockito.when(clientDataService.clientAddress(Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(Collections.singletonList(new Address()));
        List<Address> result = sut.getClientAddress(X_LOCATION_ID, CLIENT_NUM);

        Assertions.assertEquals(1, result.size());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getClient(null, CLIENT_NUM));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getClient(null,CLIENT_NUM));

    }

}
