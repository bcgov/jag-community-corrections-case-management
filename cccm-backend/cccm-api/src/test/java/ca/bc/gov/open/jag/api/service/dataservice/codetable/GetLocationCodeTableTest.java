package ca.bc.gov.open.jag.api.service.dataservice.codetable;

import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.service.CodeTableService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationList;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Collections;

public class GetLocationCodeTableTest {

    private static final String TEST_CD = "CD";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    CodeTableService sut;

    @InjectMock
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Test
    @DisplayName("Success: should return form types")
    public void testGetFormTypes() {

        CodeTable codeTable = new CodeTable(TEST_CD, TEST_VALUE);

        Mockito.when(speedmentClientService.getLocation()).thenReturn(Collections.singletonList(codeTable));

        LocationList result = sut.locationCodes();

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_CD, result.getItems().get(0).getLocationCd());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getLocationDescription());

    }

}
