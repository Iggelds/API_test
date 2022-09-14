package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;

public class AppTest {

    private final String baseUrl = "https://petstore.swagger.io/v2/";

    @Test
    public void createPetTest() {
        Pet exptectedPet = new Pet("Toby");

        Response response = request().body(exptectedPet).expect().statusCode(SC_OK)
                .when().post(baseUrl + "pet");

        Pet pet = response.jsonPath().getObject("", Pet.class);
        Assert.assertTrue(pet.getId() > 0, "Expected id exists");
        Assert.assertEquals(pet.getName(), "Toby", "Expected valid name");
    }

    private static RequestSpecification request() {
        RequestSpecification requestSpecification = RestAssured.given();
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        return requestSpecification.headers(headers);
    }
}
