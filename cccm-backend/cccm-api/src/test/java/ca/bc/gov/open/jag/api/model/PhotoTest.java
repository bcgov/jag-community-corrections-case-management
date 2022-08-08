package ca.bc.gov.open.jag.api.model;

import ca.bc.gov.open.jag.api.model.data.Photo;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class PhotoTest {

    @Test
    @DisplayName("Test Photo Model")
    public void testModel() {

        Photo sut = new Photo();
        byte[] test = "GARBAGE".getBytes();

        sut.setImage(test);
        Assertions.assertEquals(test, sut.getImage());

    }

}
