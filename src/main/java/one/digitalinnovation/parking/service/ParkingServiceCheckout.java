package one.digitalinnovation.parking.service;

import one.digitalinnovation.parking.model.entity.ParkingEntity;

public interface ParkingServiceCheckout {
    double getBill(ParkingEntity parking);
}
