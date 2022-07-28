package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.Photo;
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
    @RestClient
    ObridgeClientService obridgeClientService;

    @InjectMock
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return photo")
    public void testGetClientPhotoEndpoint() {

        byte[] image = "blarg".getBytes();

        Photo photo = new Photo();
        photo.setImage(image);
        List<Photo> photos = Collections.singletonList(photo);

        Mockito.when(obridgeClientService.getPhotosById(Mockito.any())).thenReturn(photos);
        Mockito.when(speedmentClientService.getClientId(Mockito.any())).thenReturn(BigDecimal.ONE);

        byte[] result = sut.getClientPhoto(BigDecimal.ONE);

        Assertions.assertEquals(image, sut.getClientPhoto(BigDecimal.ONE));


    }

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("404: no photo found should return 404")
    public void testGetClientPhotoNotFoundEndpoint() {

        Mockito.when(obridgeClientService.getPhotosById(Mockito.any())).thenReturn(Collections.emptyList());
        Mockito.when(speedmentClientService.getClientId(Mockito.any())).thenReturn(BigDecimal.ONE);

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
