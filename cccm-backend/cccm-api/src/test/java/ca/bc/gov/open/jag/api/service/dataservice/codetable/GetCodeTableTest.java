package ca.bc.gov.open.jag.api.service.dataservice.codetable;

import ca.bc.gov.open.jag.api.lookup.CodeTableType;
import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.service.CodeTableService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Collections;

@QuarkusTest
public class GetCodeTableTest {

    private static final String TEST_CD = "CD";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    CodeTableService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Success: should return form types")
    public void testGetFormTypes() {

        //CodeTable codeTable = new CodeTable(TEST_CD, TEST_VALUE);

        CodeList result = sut.getCodes(CodeTableType.FORM_TYPE);
        //Temporary removal unitl functionality moved to obridge
        Assertions.assertEquals(0, result.getItems().size());
        //Assertions.assertEquals(TEST_CD, result.getItems().get(0).getKey());
        //Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getValue());

    }

    @Test
    @DisplayName("Success: should return location")
    public void testGetLocations() {

        CodeTable codeTable = new CodeTable(TEST_CD, TEST_VALUE);

        CodeList result = sut.getCodes(CodeTableType.LOCATION_TYPE);
        //Temporary removal unitl functionality moved to obridge
        Assertions.assertEquals(0, result.getItems().size());
        //Assertions.assertEquals(TEST_CD, result.getItems().get(0).getKey());
        //Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getValue());


    }

    @Test
    @DisplayName("Success: should return genders")
    public void testGetGenders() {

        CodeTable codeTable = new CodeTable(TEST_CD, TEST_VALUE);

        Mockito.when(obridgeClientService.getGenderTypes()).thenReturn(Collections.singletonList(codeTable));

        CodeList result = sut.getCodes(CodeTableType.GENDER_TYPE);

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_CD, result.getItems().get(0).getKey());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getValue());

    }

    @Test
    @DisplayName("Success: should return identifiers")
    public void testGetIdentifiers() {

        CodeTable codeTable = new CodeTable(TEST_CD, TEST_VALUE);

        Mockito.when(obridgeClientService.getIdentifierTypes()).thenReturn(Collections.singletonList(codeTable));

        CodeList result = sut.getCodes(CodeTableType.IDENTIFIER_TYPE);

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_CD, result.getItems().get(0).getKey());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getValue());

    }

    @Test
    @DisplayName("Success: should return address")
    public void testGetAddress() {

        CodeTable codeTable = new CodeTable(TEST_CD, TEST_VALUE);

        Mockito.when(obridgeClientService.getAddressTypes()).thenReturn(Collections.singletonList(codeTable));

        CodeList result = sut.getCodes(CodeTableType.ADDRESS_TYPE);

        Assertions.assertEquals(1, result.getItems().size());
        Assertions.assertEquals(TEST_CD, result.getItems().get(0).getKey());
        Assertions.assertEquals(TEST_VALUE, result.getItems().get(0).getValue());

    }

}
