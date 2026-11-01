package com.fulfilment.application.monolith.location;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
@QuarkusTest
public class LocationGatewayTest {

  @Test
  public void testWhenResolveExistingLocationShouldReturn() {
     //given
     LocationGateway locationGateway = new LocationGateway();

    // when
     Location location = locationGateway.resolveByIdentifier("ZWOLLE-001");

     //then
     assertEquals("ZWOLLE-001", location.identification);
  }
}
