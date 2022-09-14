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

    @Test
    public void createPetTest() {
        RequestSpecification requestSpecification = RestAssured.given();
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        requestSpecification.headers(headers);

        Pet exptectedPet = new Pet();
        exptectedPet.setName("Toby");

        Response response = requestSpecification.body(exptectedPet).expect().statusCode(SC_OK)
                .when().post("https://petstore.swagger.io/v2/pet");

        Pet pet = response.jsonPath().getObject("", Pet.class);
        Assert.assertTrue(pet.getId() > 0, "Expected id exists");
        Assert.assertEquals(pet.getName(), "Toby", "Expected valid name");
    }
}
