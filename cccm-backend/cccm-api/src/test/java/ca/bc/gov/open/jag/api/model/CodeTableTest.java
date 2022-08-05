package ca.bc.gov.open.jag.api.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CodeTableTest {

    private static final String TEST_CD = "CD";
    private static final String TEST_VALUE = "VALUE";

    @Test
    @DisplayName("Test Code Table Model")
    public void testCodeTableModel() {

        CodeTable sut = new CodeTable("", "");
        sut.setCode(TEST_CD);
        sut.setValue(TEST_VALUE);

        Assertions.assertEquals(TEST_CD, sut.getCode());
        Assertions.assertEquals(TEST_VALUE, sut.getValue());

    }


}
