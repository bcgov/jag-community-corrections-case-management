package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.cccm.api.openapi.model.FormTypeList;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class GetFormTypesTest {

    @Inject
    FormTypesApiImpl sut;

    @Test
    @TestSecurity(user = "userOidc", roles = "data-view")
    @DisplayName("200: should return form types")
    public void testGetFormTypesEndpoint() {

        FormTypeList result = sut.getFormTypes();

        Assertions.assertEquals(2, result.getItems().size());

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
