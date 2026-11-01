package com.fulfilment.application.monolith.location;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.exceptions.LocationNotFoundException;
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

  @Test
  public void testWhenResolveUnknownLocationShouldThrow() {
    LocationGateway locationGateway = new LocationGateway();

    LocationNotFoundException exception =
        assertThrows(
            LocationNotFoundException.class,
            () -> locationGateway.resolveByIdentifier("UNKNOWN-LOCATION"));

    assertEquals(
        "Location with identifier UNKNOWN-LOCATION does not exist.", exception.getMessage());
  }
}
