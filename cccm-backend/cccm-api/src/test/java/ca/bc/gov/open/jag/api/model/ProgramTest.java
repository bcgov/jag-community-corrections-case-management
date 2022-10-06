package ca.bc.gov.open.jag.api.model;

import ca.bc.gov.open.jag.api.model.data.Program;
import ca.bc.gov.open.jag.api.model.data.Warrant;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@QuarkusTest
public class ProgramTest {

    private static final String TEST = "TEST";
    private static final BigDecimal BIG_DECIMAL = BigDecimal.ONE;

    @Test
    @DisplayName("Test Program Model")
    public void testWarrantModel() {

        Program sut = new Program();

        sut.setClientId(BIG_DECIMAL);
        sut.setEndDate(TEST);
        sut.setLocationDsc(TEST);
        sut.setLocationId(BIG_DECIMAL);
        sut.setLotyCd(TEST);
        sut.setOutcome(TEST);
        sut.setOutcomeReason(TEST);
        sut.setProgramAttendanceId(BIG_DECIMAL);
        sut.setProgramName(TEST);
        sut.setProgramSessionId(BIG_DECIMAL);
        sut.setProgramSessionStartDate(TEST);
        sut.setProgramTypeCd(TEST);
        sut.setReferralDate(TEST);
        sut.setStartDate(TEST);
        sut.setStatus(TEST);

        Assertions.assertEquals(BIG_DECIMAL,sut.getClientId());
        Assertions.assertEquals(TEST,sut.getEndDate());
        Assertions.assertEquals(TEST,sut.getLocationDsc());
        Assertions.assertEquals(BIG_DECIMAL,sut.getLocationId());
        Assertions.assertEquals(TEST,sut.getLotyCd());
        Assertions.assertEquals(TEST,sut.getOutcome());
        Assertions.assertEquals(TEST,sut.getOutcomeReason());
        Assertions.assertEquals(BIG_DECIMAL,sut.getProgramAttendanceId());
        Assertions.assertEquals(TEST,sut.getProgramName());
        Assertions.assertEquals(BIG_DECIMAL,sut.getProgramSessionId());
        Assertions.assertEquals(TEST,sut.getProgramSessionStartDate());
        Assertions.assertEquals(TEST,sut.getProgramTypeCd());
        Assertions.assertEquals(TEST,sut.getReferralDate());
        Assertions.assertEquals(TEST,sut.getStartDate());
        Assertions.assertEquals(TEST,sut.getStatus());

    }


}
