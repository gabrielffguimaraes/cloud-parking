package one.digitalinnovation.parking.service;

import one.digitalinnovation.parking.model.Parking;
import one.digitalinnovation.parking.model.dto.ExitCalTime;
import one.digitalinnovation.parking.model.dto.ParkingCreateDTO;
import one.digitalinnovation.parking.model.dto.ParkingDTO;
import one.digitalinnovation.parking.model.entity.ParkingEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ParkingService {
    static String getUUID() {
        return null;
    }

    List<ParkingDTO> findAll();

    ParkingEntity findById(String id);

    ParkingDTO create(ParkingCreateDTO parkingDTO);

    void deleteById(String id);

    ParkingDTO update(ParkingCreateDTO parkingDTO,String id);

    ParkingDTO patch(Map<Object,Object> objectMap,String id);

    ExitCalTime exit(String id);
}
