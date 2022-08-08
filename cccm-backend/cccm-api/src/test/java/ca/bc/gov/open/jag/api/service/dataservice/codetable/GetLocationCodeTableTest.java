package ca.bc.gov.open.jag.api.service.dataservice.codetable;

import ca.bc.gov.open.jag.api.service.CodeTableService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;

public class GetLocationCodeTableTest {

    private static final String TEST_CD = "CD";
    private static final String TEST_VALUE = "VALUE";

    @Inject
    CodeTableService sut;

    @InjectMock
    @RestClient
    SpeedmentClientService speedmentClientService;

}
