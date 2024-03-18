package ca.bc.gov.open.jag.api.service.dataservice.client;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.Photo;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class ClientPhotoTest {

    @Inject
    ClientDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return photo")
    public void testGetClientPhoto() {

        byte[] image = "blarg".getBytes();

        Photo mockResult = new Photo();
        mockResult.setImage(image);
        List<Photo> photos = Collections.singletonList(mockResult);

        Mockito.when(obridgeClientService.getPhotosById(Mockito.any(), Mockito.any())).thenReturn(photos);

        ca.bc.gov.open.jag.cccm.api.openapi.model.Photo result = sut.clientPhoto("1", "1");

        Assertions.assertArrayEquals(image, result.getImage());

    }

    @Test
    @DisplayName("Exception: no photo found should return 404")
    public void testGetClientPhotoNotFound() {

        Mockito.when(obridgeClientService.getPhotosById(Mockito.any(), Mockito.any())).thenReturn(Collections.emptyList());

        Assertions.assertThrows(CCCMException.class, () -> sut.clientPhoto("1", "1"));

    }

}
