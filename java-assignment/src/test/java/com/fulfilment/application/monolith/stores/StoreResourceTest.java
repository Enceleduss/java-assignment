package com.fulfilment.application.monolith.stores;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class StoreResourceTest {

  @Test
  public void testListStores() {
    List<String> storeNames =
        given().when().get("/store").then().statusCode(200).extract().jsonPath().getList("name");

    assertTrue(storeNames.containsAll(List.of("TONSTAD", "KALLAX", "BESTÅ")));
  }

  @Test
  public void testGetStoreById() {
    given()
        .when()
        .get("/store/1")
        .then()
        .statusCode(200)
        .body("name", org.hamcrest.Matchers.equalTo("TONSTAD"))
        .body("quantityProductsInStock", org.hamcrest.Matchers.equalTo(10));

    given().when().get("/store/999999").then().statusCode(404);
  }

  @Test
  public void testCreateUpdatePatchAndDeleteStore() {
    Long storeId =
        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"TEST-STORE\",\"quantityProductsInStock\":12}")
            .when()
            .post("/store")
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getLong("id");

    try {
      given()
          .when()
          .get("/store/" + storeId)
          .then()
          .statusCode(200)
          .body("name", org.hamcrest.Matchers.equalTo("TEST-STORE"))
          .body("quantityProductsInStock", org.hamcrest.Matchers.equalTo(12));

      given()
          .contentType(ContentType.JSON)
          .body("{\"name\":\"UPDATED-STORE\",\"quantityProductsInStock\":20}")
          .when()
          .put("/store/" + storeId)
          .then()
          .statusCode(200)
          .body("name", org.hamcrest.Matchers.equalTo("UPDATED-STORE"))
          .body("quantityProductsInStock", org.hamcrest.Matchers.equalTo(20));

      given()
          .contentType(ContentType.JSON)
          .body("{\"name\":\"PATCHED-STORE\",\"quantityProductsInStock\":25}")
          .when()
          .patch("/store/" + storeId)
          .then()
          .statusCode(200)
          .body("name", org.hamcrest.Matchers.equalTo("PATCHED-STORE"))
          .body("quantityProductsInStock", org.hamcrest.Matchers.equalTo(25));

      given().when().delete("/store/" + storeId).then().statusCode(204);
      given().when().get("/store/" + storeId).then().statusCode(404);
    } finally {
      given().when().delete("/store/" + storeId);
    }
  }

  @Test
  public void testRejectsInvalidStoreRequests() {
    given()
        .contentType(ContentType.JSON)
        .body("{\"id\":99,\"name\":\"INVALID-STORE\",\"quantityProductsInStock\":1}")
        .when()
        .post("/store")
        .then()
        .statusCode(422);

    given()
        .contentType(ContentType.JSON)
        .body("{\"quantityProductsInStock\":1}")
        .when()
        .put("/store/1")
        .then()
        .statusCode(422);

    given()
        .contentType(ContentType.JSON)
        .body("{\"quantityProductsInStock\":1}")
        .when()
        .patch("/store/1")
        .then()
        .statusCode(422);

    given()
        .contentType(ContentType.JSON)
        .body("{\"name\":\"MISSING-STORE\",\"quantityProductsInStock\":1}")
        .when()
        .put("/store/999999")
        .then()
        .statusCode(404);

    given()
        .contentType(ContentType.JSON)
        .body("{\"name\":\"MISSING-STORE\",\"quantityProductsInStock\":1}")
        .when()
        .patch("/store/999999")
        .then()
        .statusCode(404);

    given().when().delete("/store/999999").then().statusCode(404);
  }
}
