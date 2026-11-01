package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
public class WarehouseEndpointIT {

  @Test
  public void testListsWarehouses() {
    given()
        .when()
        .get("/warehouse")
        .then()
        .statusCode(200)
        .body(
            containsString("MWH.012"),
            containsString("MWH.023"),
            containsString("AMSTERDAM-001"),
            containsString("TILBURG-001"));
  }

  @Test
  public void testGetsWarehouseByIdAndReturnsNotFoundForUnknownId() {
    given()
        .when()
        .get("/warehouse/2")
        .then()
        .statusCode(200)
        .body("businessUnitCode", equalTo("MWH.012"))
        .body("location", equalTo("AMSTERDAM-001"))
        .body("capacity", equalTo(50))
        .body("stock", equalTo(5));

    given().when().get("/warehouse/999999").then().statusCode(404);
  }

  @Test
  public void testCreatesAndReplacesWarehouse() {
    String businessUnitCode = "IT-" + UUID.randomUUID();

    given()
        .contentType("application/json")
        .body(warehouseJson(businessUnitCode, "AMSTERDAM-002", 30, 8))
        .when()
        .post("/warehouse")
        .then()
        .statusCode(200)
        .body("businessUnitCode", equalTo(businessUnitCode))
        .body("location", equalTo("AMSTERDAM-002"))
        .body("capacity", equalTo(30))
        .body("stock", equalTo(8));

    given()
        .when()
        .get("/warehouse")
        .then()
        .statusCode(200)
        .body(containsString(businessUnitCode));

    given()
        .contentType("application/json")
        .body(warehouseJson(businessUnitCode, "HELMOND-001", 40, 8))
        .when()
        .post("/warehouse/" + businessUnitCode + "/replacement")
        .then()
        .statusCode(200)
        .body("businessUnitCode", equalTo(businessUnitCode))
        .body("location", equalTo("HELMOND-001"))
        .body("capacity", equalTo(40))
        .body("stock", equalTo(8));

    var activeBusinessUnitCodes =
        given()
            .when()
            .get("/warehouse")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("businessUnitCode", String.class);
    assertEquals(
        1L,
        activeBusinessUnitCodes.stream().filter(businessUnitCode::equals).count());
  }

  @Test
  public void testRejectsDuplicateWarehouseCode() {
    String businessUnitCode = "IT-DUPLICATE-" + UUID.randomUUID();

    given()
        .contentType("application/json")
        .body(warehouseJson(businessUnitCode, "VETSBY-001", 30, 8))
        .when()
        .post("/warehouse")
        .then()
        .statusCode(200);

    given()
        .contentType("application/json")
        .body(warehouseJson(businessUnitCode, "VETSBY-001", 30, 8))
        .when()
        .post("/warehouse")
        .then()
        .statusCode(409)
        .body("message", equalTo("Warehouse already exists"));
  }

  @Test
  public void testArchivesWarehouse() {
    given().when().delete("/warehouse/1").then().statusCode(204);

    given()
        .when()
        .get("/warehouse")
        .then()
        .statusCode(200)
        .body(
            not(containsString("ZWOLLE-001")),
            containsString("AMSTERDAM-001"),
            containsString("TILBURG-001"));
  }

  @Test
  public void testReturnsNotFoundForMissingWarehouseReplacementAndArchive() {
    given()
        .contentType("application/json")
        .body(warehouseJson("IT-MISSING", "HELMOND-001", 40, 8))
        .when()
        .post("/warehouse/IT-MISSING/replacement")
        .then()
        .statusCode(404);

    given().when().delete("/warehouse/999999").then().statusCode(404);
  }

  @Test
  public void testSpecifiedLocationIsValid() {
    given()
            .contentType("application/json")
            .body(warehouseJson("MWH.024", "HELMOND-009", 40, 8))
            .when()
            .post("/warehouse")
            .then()
            .statusCode(404)
            .body(containsString("Location with identifier HELMOND-009 does not exist."));
  }

  @Test
  public void testMaximumWarehouseLimitReachedOnLocation() {
    given()
            .contentType("application/json")
            .body(warehouseJson("MWH.024", "ZWOLLE-001", 40, 8))
            .when()
            .post("/warehouse")
            .then()
            .statusCode(409)
            .body(containsString("Maximum number of warehouses reached for this location: ZWOLLE-001"));
  }

  @Test
  public void testMaximumCapacityLimitReachedOnLocation() {
    given()
            .contentType("application/json")
            .body(warehouseJson("MWH.025", "AMSTERDAM-001", 52, 8))
            .when()
            .post("/warehouse")
            .then()
            .statusCode(409)
            .body(containsString("Adding this warehouse would exceed the maximum capacity for this location: AMSTERDAM-001 Requested capacity: 102 Maximum capacity: 100"));
  }



  private String warehouseJson(
      String businessUnitCode, String location, int capacity, int stock) {
    return String.format(
        "{\"businessUnitCode\":\"%s\",\"location\":\"%s\",\"capacity\":%d,\"stock\":%d}",
        businessUnitCode, location, capacity, stock);
  }
}
