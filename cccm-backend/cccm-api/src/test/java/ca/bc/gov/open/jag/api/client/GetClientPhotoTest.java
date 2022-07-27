package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.model.Photo;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
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
import java.io.File;
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

    @Test
    @TestSecurity(user = "userOidc", roles = "client-search")
    @DisplayName("200: should return null photo")
    public void testGetClientPhotoEndpoint() {

        Photo photo = new Photo();
        photo.setImage("blarg".getBytes());
        List<Photo> photos = Collections.singletonList(photo);

        Mockito.when(obridgeClientService.getPhotosById(Mockito.any())).thenReturn(photos);

        File result = sut.getClientPhoto(BigDecimal.ONE);

        Assertions.assertNull(sut.getClientPhoto(null));

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
