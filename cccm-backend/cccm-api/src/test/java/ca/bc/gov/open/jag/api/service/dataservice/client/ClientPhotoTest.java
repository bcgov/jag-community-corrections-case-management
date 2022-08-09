package ca.bc.gov.open.jag.api.service.dataservice.client;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.Photo;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
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
import java.util.List;

@QuarkusTest
public class ClientPhotoTest {

    @Inject
    ClientDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @InjectMock
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Test
    @DisplayName("Success: should return photo")
    public void testGetClientPhoto() {

        byte[] image = "blarg".getBytes();

        Photo photo = new Photo();
        photo.setImage(image);
        List<Photo> photos = Collections.singletonList(photo);

        Mockito.when(obridgeClientService.getPhotosById(Mockito.any())).thenReturn(photos);
        Mockito.when(speedmentClientService.getClientId(Mockito.any())).thenReturn(BigDecimal.ONE);

        Assertions.assertEquals(image, sut.clientPhoto(BigDecimal.ONE));


    }

    @Test
    @DisplayName("Exception: no photo found should return 404")
    public void testGetClientPhotoNotFound() {

        Mockito.when(obridgeClientService.getPhotosById(Mockito.any())).thenReturn(Collections.emptyList());
        Mockito.when(speedmentClientService.getClientId(Mockito.any())).thenReturn(BigDecimal.ONE);

        Assertions.assertThrows(CCCMException.class, () -> sut.clientPhoto(BigDecimal.ONE));

    }

}
