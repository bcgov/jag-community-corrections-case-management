package ca.bc.gov.open.jag.api.util.form;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ca.bc.gov.open.jag.api.util.FormUtil.ratingToInteger;


public class RatingToIntegerTest {

    @Test
    @DisplayName("Success: should return rating integer H")
    public void testSuccessCalculateRatingHigh() {

        Assertions.assertEquals(3, ratingToInteger("H"));

    }

    @Test
    @DisplayName("Success: should return rating integer M")
    public void testSuccessCalculateRatingMedium() {

        Assertions.assertEquals(2, ratingToInteger("M"));

    }



    @Test
    @DisplayName("Success: should return rating integer L")
    public void testSuccessCalculateRatingLow() {

        Assertions.assertEquals(1, ratingToInteger("L"));

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
