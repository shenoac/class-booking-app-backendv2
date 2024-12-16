package com.mycompany.controller;

import com.github.javafaker.Faker;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;

@QuarkusTest
public class UserResourceH2Test {

    private static Faker faker;

    @BeforeAll
    public static void setup() {
        System.setProperty("quarkus.datasource.db-kind", "h2");
        System.setProperty("quarkus.datasource.jdbc.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        System.setProperty("quarkus.datasource.username", "sa");
        System.setProperty("quarkus.datasource.password", "");
        System.setProperty("quarkus.hibernate-orm.database.generation", "drop-and-create");

        faker = new Faker();
    }

    @Test
    public void testRegisterBuyerSuccess() {
        String uniqueEmail = faker.internet().emailAddress();

        String payload = String.format("""
            {
                "email": "%s",
                "password": "password123",
                "name": "Test Buyer"
            }
        """, uniqueEmail);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/auth/register/buyer")
                .then()
                .statusCode(200)
                .body(containsString("User successfully registered with role: BUYER"));
    }
}
