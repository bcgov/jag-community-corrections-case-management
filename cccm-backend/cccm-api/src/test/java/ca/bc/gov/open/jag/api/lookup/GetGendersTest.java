package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.api.service.CodeTableService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
@QuarkusTest
public class GetGendersTest {

    private static final String TEST_CD = "CD";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    LookupApiImpl sut;

    @InjectMock
    CodeTableService codeTableService;

    @Test
    @TestSecurity(user = "userOidc", roles = "data-view")
    @DisplayName("200: should return gender types")
    public void testGetGenderTypesEndpoint() {

        CodeList genderList = new CodeList();

        Code gender = new Code();
        gender.setKey(TEST_CD);
        gender.setValue(TEST_VALUE);
        genderList.setItems(Collections.singletonList(gender));


        Mockito.when(codeTableService.getCodes(any())).thenReturn(genderList);

        CodeList result = sut.getGenders(null);

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_CD, result.getItems().get(0).getKey());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getValue());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getGendersTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getGenders(null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getGendersTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getGenders(null));

    }

}
