package com.mycompany;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class UserControllerTest {

    @Test
    public void testLoginEndpoint() {
        RestAssured.given()
                .contentType("application/json")
                .body("{\"email\":\"shenoachee@gmail.com\", \"password\":\"Password123\"}") // Test user credentials
                .when().post("/auth/login")
                .then()
                .statusCode(200)                        // Expecting HTTP 200 OK if login is successful
                .body("token", notNullValue());         // Check that a "token" is present in the response
    }
}
