package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import static io.restassured.RestAssured.given;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusIntegrationTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FulfilmentUnitEndpointIT {

    @Test
    @Order(1)
    public void testProductCanBeFulfilledByMaximumTwoWarehousePerStore() {
        given()
                .contentType("application/json")
                .body(fulfilmentUnitJson(1,"TONSTAD","MWH.012"))
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(204);
        given()
                .contentType("application/json")
                .body(fulfilmentUnitJson(1,"TONSTAD","MWH.001"))
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(204);
        given()
                .contentType("application/json")
                .body(fulfilmentUnitJson(1,"TONSTAD","MWH.023"))
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(409);
    }
    @Test
    @Order(2)
    public void testStoreCanBeFulfilledByMaximumThreeWarehouses() {
//        given()
//                .contentType("application/json")
//                .body(fulfilmentUnitJson(1,"TONSTAD","MWH.012"))
//                .when()
//                .post("/fulfilment")
//                .then()
//                .statusCode(204);
//        given()
//                .contentType("application/json")
//                .body(fulfilmentUnitJson(1,"TONSTAD","MWH.001"))
//                .when()
//                .post("/fulfilment")
//                .then()
//                .statusCode(204);
        given()
                .contentType("application/json")
                .body(fulfilmentUnitJson(2,"TONSTAD","MWH.023"))
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(204);
        given()
                .contentType("application/json")
                .body(fulfilmentUnitJson(3,"TONSTAD","MWH.093"))
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(409);
}


    @Test
    @Order(3)
    public void testAWarehouseCanCanMaximallyStoreFiveProducts() {

        given()
                .contentType("application/json")
                .body(fulfilmentUnitJson(2,"KALLAX","MWH.012"))
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(204);
        given()
                .contentType("application/json")
                .body(fulfilmentUnitJson(3,"KALLAX","MWH.012"))
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(204);
        given()
                .contentType("application/json")
                .body(fulfilmentUnitJson(4,"KALLAX","MWH.012"))
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(204);
        given()
                .contentType("application/json")
                .body(fulfilmentUnitJson(5,"KALLAX","MWH.012"))
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(204);
        given()
                .contentType("application/json")
                .body(fulfilmentUnitJson(6,"KALLAX","MWH.012"))
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(409);
    }
private String fulfilmentUnitJson(
            int productId, String storeId, String warehouseBusinessUnitCode) {
        return String.format(
                "{\"productId\":\"%d\",\"storeId\":\"%s\",\"warehouseBusinessUnitCode\":\"%s\"}",
                productId, storeId, warehouseBusinessUnitCode);
    }
}
