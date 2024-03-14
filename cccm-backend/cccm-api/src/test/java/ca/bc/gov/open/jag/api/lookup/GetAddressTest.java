package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.api.service.CodeTableService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
@QuarkusTest
public class GetAddressTest {

    private static final String TEST_CD = "CD";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    LookupApiImpl sut;

    @InjectMock
    CodeTableService codeTableService;

    @Test
    @TestSecurity(user = "userOidc", roles = "data-view")
    @DisplayName("200: should return Address types")
    public void testGetAddressEndpoint() {

        CodeList addressList = new CodeList();

        Code address = new Code();
        address.setKey(TEST_CD);
        address.setValue(TEST_VALUE);
        addressList.setItems(Collections.singletonList(address));


        Mockito.when(codeTableService.getCodes(any())).thenReturn(addressList);

        CodeList result = sut.getAddress(null);

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_CD, result.getItems().get(0).getKey());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getValue());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getAddressTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getAddress(null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getAddressTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getAddress(null));

    }

}
