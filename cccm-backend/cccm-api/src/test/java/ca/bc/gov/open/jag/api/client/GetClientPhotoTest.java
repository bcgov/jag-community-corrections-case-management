package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.Photo;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
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
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class GetClientPhotoTest {

    @Inject
    ClientsApiImpl sut;

    @InjectMock
    ClientDataService clientDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return photo")
    public void testGetClientPhotoEndpoint() {

        byte[] image = "blarg".getBytes();

        Mockito.when(clientDataService.clientPhoto(Mockito.any())).thenReturn(image);

        Assertions.assertEquals(image, sut.getClientPhoto(BigDecimal.ONE));


    }

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("404: no photo found should return 404")
    public void testGetClientPhotoNotFoundEndpoint() {

        Mockito.when(clientDataService.clientPhoto(Mockito.any())).thenThrow(new CCCMException("Not found", CCCMErrorCode.RECORDNOTFOUND));

        Assertions.assertThrows(CCCMException.class, () -> sut.getClientPhoto(BigDecimal.ONE));

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getClientPhoto(null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getClientPhoto(null));

    }

}
