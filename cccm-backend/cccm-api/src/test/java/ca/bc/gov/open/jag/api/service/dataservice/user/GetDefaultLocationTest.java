package ca.bc.gov.open.jag.api.service.dataservice.user;

import ca.bc.gov.open.jag.api.lookup.CodeTableType;
import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.service.CodeTableService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import ca.bc.gov.open.jag.api.service.UserDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@QuarkusTest
public class GetDefaultLocationTest {

    private static final BigDecimal TEST_CD = BigDecimal.ONE;
    private static final String TEST_VALUE = "VALUE";

    @Inject
    UserDataService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return form types")
    public void testGetFormTypes() {

        Map map = new HashMap();
        map.put("locationId", TEST_CD);
        map.put("locationText", TEST_VALUE);

        Mockito.when(obridgeClientService.getLocation()).thenReturn(map);

        Code result = sut.getDefaultLocation("user");

        Assertions.assertEquals(TEST_CD.toPlainString(), result.getKey());
        Assertions.assertEquals(TEST_VALUE, result.getValue());

    }

}
