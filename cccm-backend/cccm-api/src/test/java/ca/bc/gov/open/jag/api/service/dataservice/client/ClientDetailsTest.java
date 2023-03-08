package ca.bc.gov.open.jag.api.service.dataservice.client;

import ca.bc.gov.open.jag.api.model.data.Address;
import ca.bc.gov.open.jag.api.model.data.Photo;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
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

@QuarkusTest
public class ClientDetailsTest {

    private static final String TEST_NAME = "TEST1, TESTER";
    private static final String GENDER_CODE = "M";
    private static final String COMMUNITY_LOCATION = "Community Location";
    private static final String ADDRESS = "Address";
    private static final String CASE_MANAGER = "Case Manager";
    private static final String TEST_VALUE = "VALUE";
    private static final String CLIENT_NUM = "01";
    private static final String TEST_IDIR = "test@idir";
    private static final String LOCATION = "123";
    private static final String CLIENT_NO = "01";
    private static final String PHOTO_TAKEN_DATE = "TEST";
    private static final byte[] BYTES = "blarg".getBytes();

    @Inject
    ClientDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return clients details")
    public void testGetClientDetails() {

        Mockito.when(obridgeClientService.getDetailsById(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClient());
        Mockito.when(obridgeClientService.getAddressById(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Collections.singletonList(createAddress()));
        Mockito.when(obridgeClientService.getPhotosById(Mockito.any(), Mockito.any())).thenReturn(Collections.singletonList(createPhoto()));

        Client result = sut.clientDetails(CLIENT_NUM, TEST_IDIR, LOCATION);

        Assertions.assertEquals(TEST_NAME, result.getClientName());
        Assertions.assertEquals(GENDER_CODE, result.getGender());
        Assertions.assertEquals(TEST_VALUE, result.getAlias());
        Assertions.assertEquals(COMMUNITY_LOCATION, result.getCommunityInformation().getCommunityLocation());
        Assertions.assertEquals(CASE_MANAGER, result.getCommunityInformation().getCaseManager());
        Assertions.assertEquals(PHOTO_TAKEN_DATE, result.getPhoto().getPhotoTakenDate());
        Assertions.assertEquals(BYTES.length, result.getPhoto().getImage().length);
        Assertions.assertEquals(1, result.getAddress().size());
        Assertions.assertEquals(ADDRESS, result.getAddress().get(0).getFullAddress());
        Assertions.assertTrue(result.getAddress().get(0).getExpired());

    }

    @Test
    @DisplayName("Success: should return clients details no photo")
    public void testGetClientDetailsNoPhoto() {

        Mockito.when(obridgeClientService.getDetailsById(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(createClient());
        Mockito.when(obridgeClientService.getAddressById(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Collections.singletonList(createAddress()));
        Mockito.when(obridgeClientService.getPhotosById(Mockito.any(), Mockito.any())).thenReturn(Collections.EMPTY_LIST);

        Client result = sut.clientDetails(CLIENT_NUM, TEST_IDIR, LOCATION);

        Assertions.assertEquals(TEST_NAME, result.getClientName());
        Assertions.assertEquals(GENDER_CODE, result.getGender());
        Assertions.assertEquals(TEST_VALUE, result.getAlias());
        Assertions.assertEquals(COMMUNITY_LOCATION, result.getCommunityInformation().getCommunityLocation());
        Assertions.assertEquals(CASE_MANAGER, result.getCommunityInformation().getCaseManager());
        Assertions.assertNull(result.getPhoto());
        Assertions.assertEquals(1, result.getAddress().size());
        Assertions.assertEquals(ADDRESS, result.getAddress().get(0).getFullAddress());
        Assertions.assertTrue(result.getAddress().get(0).getExpired());

    }



    private ca.bc.gov.open.jag.api.model.data.Client createClient() {

        ca.bc.gov.open.jag.api.model.data.Client client1 = new ca.bc.gov.open.jag.api.model.data.Client();
        client1.setClientNo(CLIENT_NO);
        client1.setCurrentName(TEST_NAME);
        client1.setGenderCode(GENDER_CODE);
        client1.setAlias(TEST_VALUE);
        client1.setCommunityLocation(COMMUNITY_LOCATION);
        client1.setCaseManager(CASE_MANAGER);

        return client1;

    }

    private ca.bc.gov.open.jag.api.model.data.Address createAddress() {

        LocalDate testDate = LocalDate.now();
        testDate = testDate.minusYears(1);

        Address address = new Address();
        address.setFullAddress(ADDRESS);
        address.setExpiryDate(testDate.toString());

        return address;

    }

    private ca.bc.gov.open.jag.api.model.data.Photo createPhoto() {

        Photo photo = new Photo();
        photo.setImage(BYTES);
        photo.setPhotoTakenDate(PHOTO_TAKEN_DATE);

        return photo;

    }

}
