package ca.bc.gov.open.jag.api.util.form;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ca.bc.gov.open.jag.api.util.FormUtils.ratingToInteger;

public class RatingToIntegerTest {

    @Test
    @DisplayName("Success: should return rating integer H")
    public void testSuccessCalculateRatingHigh1() {

        Assertions.assertEquals(3, ratingToInteger("H"));

    }

    @Test
    @DisplayName("Success: should return rating integer 4a")
    public void testSuccessCalculateRatingHigh2() {

        Assertions.assertEquals(3, ratingToInteger("4a"));

    }

    @Test
    @DisplayName("Success: should return rating integer 4b")
    public void testSuccessCalculateRatingHigh3() {

        Assertions.assertEquals(3, ratingToInteger("4b"));

    }

    @Test
    @DisplayName("Success: should return rating integer 4aHE")
    public void testSuccessCalculateRatingHigh4() {

        Assertions.assertEquals(3, ratingToInteger("4aHE"));

    }

    @Test
    @DisplayName("Success: should return rating integer 4bLE")
    public void testSuccessCalculateRatingHigh5() {

        Assertions.assertEquals(3, ratingToInteger("4bLE"));

    }


    @Test
    @DisplayName("Success: should return rating integer M")
    public void testSuccessCalculateRatingMedium1() {

        Assertions.assertEquals(2, ratingToInteger("M"));

    }

    @Test
    @DisplayName("Success: should return rating integer 4aAE")
    public void testSuccessCalculateRatingMedium2() {

        Assertions.assertEquals(2, ratingToInteger("4aAE"));

    }

    @Test
    @DisplayName("Success: should return rating integer 3AE")
    public void testSuccessCalculateRatingMedium3() {

        Assertions.assertEquals(2, ratingToInteger("3AE"));

    }

    @Test
    @DisplayName("Success: should return rating integer 3")
    public void testSuccessCalculateRatingMedium4() {

        Assertions.assertEquals(2, ratingToInteger("3"));

    }

    @Test
    @DisplayName("Success: should return rating integer L")
    public void testSuccessCalculateRatingLow1() {

        Assertions.assertEquals(1, ratingToInteger("L"));

    }

    @Test
    @DisplayName("Success: should return rating integer 1AE")
    public void testSuccessCalculateRatingLow2() {

        Assertions.assertEquals(1, ratingToInteger("1AE"));

    }

    @Test
    @DisplayName("Success: should return rating integer 1HE")
    public void testSuccessCalculateRatingLow3() {

        Assertions.assertEquals(1, ratingToInteger("1HE"));

    }

    @Test
    @DisplayName("Success: should return rating integer 2AE")
    public void testSuccessCalculateRatingLow4() {

        Assertions.assertEquals(1, ratingToInteger("2AE"));

    }

    @Test
    @DisplayName("Success: should return rating integer 2HE")
    public void testSuccessCalculateRatingLow5() {

        Assertions.assertEquals(1, ratingToInteger("2HE"));

    }

    @Test
    @DisplayName("Success: should return rating integer 1")
    public void testSuccessCalculateRatingLow6() {

        Assertions.assertEquals(1, ratingToInteger("1"));

    }

    @Test
    @DisplayName("Success: should return rating integer 2")
    public void testSuccessCalculateRatingLow7() {

        Assertions.assertEquals(1, ratingToInteger("2"));

    }

    @Test
    @DisplayName("Success: should return rating integer Default")
    public void testSuccessCalculateRatingDefault() {

        Assertions.assertEquals(0, ratingToInteger("QQ"));

    }

    @Test
    @DisplayName("Success: should return rating integer Null or Empty")
    public void testSuccessCalculateRatingEmpty() {

        Assertions.assertEquals(-1 , ratingToInteger(null));

    }

}
