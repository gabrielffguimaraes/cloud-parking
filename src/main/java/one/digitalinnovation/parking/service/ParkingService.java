package one.digitalinnovation.parking.service;

import one.digitalinnovation.parking.model.dto.ParkingDTO;

import java.util.List;

public interface ParkingService {
    static String getUUID() {
        return null;
    }

    List<ParkingDTO> findAll();
}
