package one.digitalinnovation.parking.service.impl;

import one.digitalinnovation.parking.helpers.Helper;
import one.digitalinnovation.parking.model.Parking;
import one.digitalinnovation.parking.model.dto.ParkingDTO;
import one.digitalinnovation.parking.service.ParkingService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ParkingServiceImp implements ParkingService {
    private final Helper helper;
    public ParkingServiceImp(Helper helper) {
        this.helper = helper;
    }

    private  static Map<String, Parking> parkingMap = new LinkedHashMap<>();

    static {
        String id = getUUID();
        Parking parking = new Parking(id, "DMS-1111", "SC" , "CELTA" , "PRETO");
        parkingMap.put(id,parking);
    }

    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public List<ParkingDTO> findAll() {
        List<Parking> parkingList = parkingMap.values().stream().collect(Collectors.toList());

        List<ParkingDTO> result = helper.mapList(parkingList,ParkingDTO.class);

        return result;
    }


}
