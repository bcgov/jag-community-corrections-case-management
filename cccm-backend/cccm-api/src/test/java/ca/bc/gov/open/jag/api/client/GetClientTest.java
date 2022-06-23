package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientDetails;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;

@QuarkusTest
public class GetClientTest {

    @Inject
    ClientsApiImpl sut;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-view")
    @DisplayName("200: should return client")
    public void testGetClientEndpoint() {

        ClientDetails result = sut.getClient(BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result.getClientId());
        Assertions.assertEquals("John", result.getFirstName());
        Assertions.assertEquals("Smith", result.getLastName());
        Assertions.assertEquals("M", result.getGender());
        Assertions.assertEquals("Vancouver, BC", result.getLocation());
        Assertions.assertEquals(LocalDate.of(1982, 3, 4), result.getBirthDate());
        Assertions.assertEquals(LocalDate.of(2021, 10, 2), result.getFinalOrderExpiryDate());

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

}
