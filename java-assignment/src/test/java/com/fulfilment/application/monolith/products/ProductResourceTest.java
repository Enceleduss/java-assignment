package com.fulfilment.application.monolith.products;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ProductResourceTest {

  @Test
  void createsGetsUpdatesAndDeletesProduct() {
    long productId =
        given()
            .contentType(ContentType.JSON)
            .body(
                "{\"name\":\"COVERAGE-PRODUCT\","
                    + "\"description\":\"created in test\","
                    + "\"price\":19.95,\"stock\":7}")
            .when()
            .post("/product")
            .then()
            .statusCode(201)
            .body("name", equalTo("COVERAGE-PRODUCT"))
            .extract()
            .jsonPath()
            .getLong("id");

    given()
        .when()
        .get("/product/" + productId)
        .then()
        .statusCode(200)
        .body("description", equalTo("created in test"))
        .body("stock", equalTo(7));

    given()
        .contentType(ContentType.JSON)
        .body(
            "{\"name\":\"UPDATED-COVERAGE-PRODUCT\","
                + "\"description\":\"updated in test\","
                + "\"price\":29.95,\"stock\":9}")
        .when()
        .put("/product/" + productId)
        .then()
        .statusCode(200)
        .body("name", equalTo("UPDATED-COVERAGE-PRODUCT"))
        .body("description", equalTo("updated in test"))
        .body("stock", equalTo(9));

    given().when().delete("/product/" + productId).then().statusCode(204);
    given().when().get("/product/" + productId).then().statusCode(404);
  }

  @Test
  void rejectsInvalidProductRequests() {
    given()
        .contentType(ContentType.JSON)
        .body("{\"id\":99,\"name\":\"INVALID-PRODUCT\"}")
        .when()
        .post("/product")
        .then()
        .statusCode(422);

    given()
        .contentType(ContentType.JSON)
        .body("{\"description\":\"missing name\"}")
        .when()
        .put("/product/1")
        .then()
        .statusCode(422);

    given()
        .contentType(ContentType.JSON)
        .body("{\"name\":\"MISSING-PRODUCT\"}")
        .when()
        .put("/product/999999")
        .then()
        .statusCode(404);

    given().when().get("/product/999999").then().statusCode(404);
    given().when().delete("/product/999999").then().statusCode(404);
  }
}
