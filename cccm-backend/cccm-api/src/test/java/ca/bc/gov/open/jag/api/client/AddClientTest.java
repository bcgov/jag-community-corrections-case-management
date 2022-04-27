package ca.bc.gov.open.jag.api.client;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class AddClientTest {

    @Test
    public void testClientsAddEndpoint() {
        given()
                .when().post("/clients")
                .then()
                .statusCode(201)
                .body(is(null));
    }

}
