package one.digitalinnovation.parking.service.impl;

import one.digitalinnovation.parking.helpers.Helper;
import one.digitalinnovation.parking.model.Parking;
import one.digitalinnovation.parking.model.dto.ParkingCreateDTO;
import one.digitalinnovation.parking.model.dto.ParkingDTO;
import one.digitalinnovation.parking.service.ParkingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ParkingServiceImp implements ParkingService {
    private final Helper helper;
    public ParkingServiceImp(Helper helper) {
        this.helper = helper;
    }

    private  static Map<String, Parking> parkingDB = new LinkedHashMap<>();

    static {
        Parking parking1 = new Parking(getUUID(), "DMS-1111", "SC" , "CELTA" , "PRETO",LocalDateTime.now());
        Parking parking2 = new Parking(getUUID(), "GPS-5454S", "RJ" , "FORD FIESTA" , "BRANCO",LocalDateTime.now());

        parkingDB.put(parking1.getId(),parking1);
        parkingDB.put(parking2.getId(),parking2);
    }

    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public List<ParkingDTO> findAll() {
        List<Parking> parkingList = parkingDB.values().stream().collect(Collectors.toList());

        List<ParkingDTO> result = helper.mapList(parkingList,ParkingDTO.class);

        return result;
    }
    public ParkingDTO findById(String id) {
        Parking parking = parkingDB.get(id);
        System.out.println(parking.toString());
        ParkingDTO result = helper.map(parking,ParkingDTO.class);
        return result;
    }

    public ParkingDTO create(ParkingCreateDTO parkingCreate) {
        Parking parking = helper.map(parkingCreate,Parking.class);
        parking.setId(getUUID());
        parking.setEntryDate(LocalDateTime.now());
        parkingDB.put(parking.getId(),parking);
        return helper.map(parking,ParkingDTO.class);
    }

}
