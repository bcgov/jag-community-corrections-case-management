package ca.bc.gov.open.jag.api.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

@QuarkusTest
public class AlertTest {

    @Test
    @DisplayName("Test Alert Model")
    public void testModel() {

        LocalDate testDate = LocalDate.now();

        Alert sut = new Alert();
        sut.setCommentTxt("TEST");
        sut.setEffectiveDt(Date.valueOf(testDate));

        Assertions.assertEquals("TEST", sut.getCommentTxt());
        Assertions.assertEquals(Date.valueOf(testDate), sut.getEffectiveDt());

    }

}
