package ca.bc.gov.open.jag.api.user;

import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.PO;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class GetPoListTest {

    private static final String TEST_IDIR = "IDIR";
    private static final String TEST_PONAME = "PONAME";

    @Inject
    UserApiImpl sut;

    @InjectMock
    UserDataService userDataService;

    @Test
    @TestSecurity(user = "userOidc", roles = "data-view")
    @DisplayName("200: should return PO list")
    public void testGetPOsEndpoint() {

        PO po = new PO();
        po.setIdirId(TEST_IDIR);
        po.setPoName(TEST_PONAME);

        Mockito.when(userDataService.getPOList(any(), any())).thenReturn(Collections.singletonList(po));

        List<PO> result = sut.getPOs("1");

        Assertions.assertEquals(TEST_IDIR, result.get(0).getIdirId());
        Assertions.assertEquals(TEST_PONAME, result.get(0).getPoName());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getPOsTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getPOs("1"));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getPOsTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getPOs("1"));

    }

}
