package ca.bc.gov.open.jag.api.service.dataservice.user;

import ca.bc.gov.open.jag.api.model.data.User;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
@QuarkusTest
public class GetPOListTest {

    private static final String TEST_PO_FIRST = "TEST_PO_FIRST";
    private static final String TEST_PO_LAST = "TEST_PO_LAST";
    private static final String TEST_IDIR = "TEST_IDIR";
    private static final int INT_TEST = 1;

    @Inject
    UserDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return data")
    public void testSupervisorDashboard() {

        Mockito.when(obridgeClientService.getPOUsers(Mockito.any())).thenReturn(createUsers());

        List<ca.bc.gov.open.jag.cccm.api.openapi.model.PO> result = sut.getPOList("test@idir", BigDecimal.ONE);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("TEST_PO_FIRST TEST_PO_LAST", result.get(0).getPoName());
        Assertions.assertEquals(TEST_IDIR, result.get(0).getIdirId());

    }

    private List<User> createUsers() {

        ca.bc.gov.open.jag.api.model.data.User user = new ca.bc.gov.open.jag.api.model.data.User();
        user.setFirstName(TEST_PO_FIRST);
        user.setLastName(TEST_PO_LAST);
        user.setIdirId(TEST_IDIR);


        return Collections.singletonList(user);

    }

}
