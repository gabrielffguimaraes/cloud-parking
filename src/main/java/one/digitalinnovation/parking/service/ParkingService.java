package one.digitalinnovation.parking.service;

import one.digitalinnovation.parking.model.Parking;
import one.digitalinnovation.parking.model.dto.ParkingCreateDTO;
import one.digitalinnovation.parking.model.dto.ParkingDTO;

import java.util.List;
import java.util.Map;

public interface ParkingService {
    static String getUUID() {
        return null;
    }

    List<ParkingDTO> findAll();

    ParkingDTO findById(String id);

    ParkingDTO create(ParkingCreateDTO parkingDTO);

    void deleteById(String id);

    ParkingDTO update(ParkingCreateDTO parkingDTO,String id);

    ParkingDTO patch(Map<Object,Object> objectMap,String id);

    void exit(String id);
}
