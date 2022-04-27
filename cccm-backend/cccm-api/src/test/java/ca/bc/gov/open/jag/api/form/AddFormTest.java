package ca.bc.gov.open.jag.api.form;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class AddFormTest {

    @Test
    @DisplayName("204: form added")
    public void testClientsAddEndpoint() {
        given()
                .when().header("Content-Type", "application/json")
                .body("{}")
                .post("/forms")
                .then()
                .statusCode(200);
    }

}
