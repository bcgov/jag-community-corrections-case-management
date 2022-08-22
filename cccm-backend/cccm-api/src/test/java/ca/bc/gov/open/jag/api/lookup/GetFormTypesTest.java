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
public class GetFormTypesTest {

    private static final String TEST_CD = "CD";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    LookupApiImpl sut;

    @InjectMock
    CodeTableService codeTableService;

    @Test
    @TestSecurity(user = "userOidc", roles = "data-view")
    @DisplayName("200: should return form types")
    public void testGetFormTypesEndpoint() {

        CodeList formTypeList = new CodeList();

        Code formType = new Code();
        formType.setCode(TEST_CD);
        formType.setDescription(TEST_VALUE);
        formTypeList.setItems(Collections.singletonList(formType));


        Mockito.when(codeTableService.getCodes(any())).thenReturn(formTypeList);

        CodeList result = sut.getFormTypes();

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_CD, result.getItems().get(0).getCode());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getDescription());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getFormTypesTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getFormTypes());

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getFormTypesTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getFormTypes());

    }

}
