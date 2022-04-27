package ca.bc.gov.open.jag.api.client;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class AddClientTest {

    @Test
    @DisplayName("204: client added")
    public void testClientsAddEndpoint() {
        given()
                .when().header("Content-Type", "application/json")
                .body("{}")
                .post("/clients")
                .then()
                .statusCode(204);
    }

}
