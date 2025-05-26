package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.api.service.CodeTableService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class GetCityTest {

    private static final String TEST_CD = "CD";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    LookupApiImpl sut;

    @InjectMock
    CodeTableService codeTableService;

    @Test
    @TestSecurity(user = "userOidc", roles = "data-view")
    @DisplayName("200: should return city types")
    public void testGetCityEndpoint() {

        CodeList cityList = new CodeList();

        Code city = new Code();
        city.setKey(TEST_CD);
        city.setValue(TEST_VALUE);
        cityList.setItems(Collections.singletonList(city));


        Mockito.when(codeTableService.getCodes(any())).thenReturn(cityList);

        CodeList result = sut.getCity(null);

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_CD, result.getItems().get(0).getKey());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getValue());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getCityTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getCity(null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getCityTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getCity(null));

    }

}
