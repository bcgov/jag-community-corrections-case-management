package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormTypeList;
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
import java.util.Collections;

@QuarkusTest
public class GetFormTypesTest {

    private static final String TEST_CD = "CD";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    FormTypesApiImpl sut;

    @InjectMock
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Test
    @TestSecurity(user = "userOidc", roles = "data-view")
    @DisplayName("200: should return form types")
    public void testGetFormTypesEndpoint() {

        CodeTable codeTable = new CodeTable(TEST_CD, TEST_VALUE);

        Mockito.when(speedmentClientService.getFormTypes()).thenReturn(Collections.singletonList(codeTable));

        FormTypeList result = sut.getFormTypes();

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_CD, result.getItems().get(0).getTypeCd());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getTypeDescription());

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
