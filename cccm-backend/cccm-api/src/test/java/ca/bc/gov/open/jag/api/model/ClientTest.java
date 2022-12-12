package ca.bc.gov.open.jag.api.model;

import ca.bc.gov.open.jag.api.model.data.Client;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ClientTest {

    private static final String CLIENT_NO = "01";
    private static final String TEST_STRING = "TEST";

    @Test
    @DisplayName("Test Client Model")
    public void testClientModel() {

        Client sut = new Client();

        sut.setClientNo(CLIENT_NO);
        sut.setClientName(TEST_STRING);
        sut.setCurrentName(TEST_STRING);
        sut.setCurrentNameYn(TEST_STRING);
        sut.setGenderCode(TEST_STRING);
        sut.setBirthDate(TEST_STRING);
        sut.setCustodyLocation(TEST_STRING);
        sut.setCommunityLocation(TEST_STRING);
        sut.setAlias(TEST_STRING);
        sut.setSealed(TEST_STRING);
        sut.setAddressStatus(TEST_STRING);
        sut.setAddressType(TEST_STRING);
        sut.setAddressExpiry(TEST_STRING);

        Assertions.assertEquals(CLIENT_NO, sut.getClientNo());
        Assertions.assertEquals(TEST_STRING, sut.getClientName());
        Assertions.assertEquals(TEST_STRING, sut.getCurrentName());
        Assertions.assertEquals(TEST_STRING, sut.getCurrentNameYn());
        Assertions.assertEquals(TEST_STRING, sut.getGenderCode());
        Assertions.assertEquals(TEST_STRING, sut.getBirthDate());
        Assertions.assertEquals(TEST_STRING, sut.getCustodyLocation());
        Assertions.assertEquals(TEST_STRING, sut.getCommunityLocation());
        Assertions.assertEquals(TEST_STRING, sut.getAlias());
        Assertions.assertEquals(TEST_STRING, sut.getSealed());
        Assertions.assertEquals(TEST_STRING, sut.getAddressStatus());
        Assertions.assertEquals(TEST_STRING, sut.getAddressType());
        Assertions.assertEquals(TEST_STRING, sut.getAddressExpiry());

    }

}
