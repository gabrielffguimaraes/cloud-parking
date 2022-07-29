package one.digitalinnovation.parking.service.impl;

import one.digitalinnovation.parking.helpers.Helper;
import one.digitalinnovation.parking.model.Parking;
import one.digitalinnovation.parking.model.dto.ExitCalTime;
import one.digitalinnovation.parking.model.dto.ParkingCreateDTO;
import one.digitalinnovation.parking.model.dto.ParkingDTO;
import one.digitalinnovation.parking.service.ParkingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ParkingServiceImp implements ParkingService {
    private final Helper helper;
    public ParkingServiceImp(Helper helper) {
        this.helper = helper;
    }

    private  static Map<String, Parking> parkingDB = new LinkedHashMap<>();

    @Value("${messages.parking-not-found}")
    private String parkingNotFound;
    @Value("${messages.operation-already-done}")
    private String operationAlreadyDone;

    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }
    @Override
    public List<ParkingDTO> findAll() {
        List<Parking> parkingList = parkingDB.values().stream().collect(Collectors.toList());

        List<ParkingDTO> result = helper.mapList(parkingList,ParkingDTO.class);

        return result;
    }
    @Override
    public ParkingDTO findById(String id) {
        Parking parking = parkingDB.get(id);
        if(parking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,parkingNotFound);
        }

        System.out.println(parking.toString());
        ParkingDTO result = helper.map(parking,ParkingDTO.class);
        return result;
    }
    @Override
    public ParkingDTO create(ParkingCreateDTO parkingCreate) {
        Parking parking = helper.map(parkingCreate,Parking.class);
        parking.setId(getUUID());
        parking.setEntryDate(LocalDateTime.now());
        parkingDB.put(parking.getId(),parking);
        return helper.map(parking,ParkingDTO.class);
    }

    @Override
    public void deleteById(String id) {
        if(findById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,parkingNotFound);
        }
        parkingDB.remove(id);
    }
    @Override
    public ParkingDTO update(ParkingCreateDTO parkingDTO,String id) {
        Parking parking = parkingDB.get(id);
        if(parking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,parkingNotFound);
        }
        parking = helper.map(parkingDTO,Parking.class);
        parking.setId(id);
        parkingDB.put(id,parking);

        return helper.map(parking,ParkingDTO.class);
    }
    @Override
    public ParkingDTO patch(Map<Object,Object> objectMap,String id) {
        Parking parking = parkingDB.get(id);
        if(parking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,parkingNotFound);
        }
        objectMap.forEach((key , value) -> {
            if(ReflectionUtils.findField(ParkingDTO.class,(String) key) != null) {
                Field field = ReflectionUtils.findField(Parking.class,(String) key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, parking, value);
            }
        });

        parkingDB.put(id,parking);
        return helper.map(parking,ParkingDTO.class);
    }

    public ExitCalTime exit(String id) {
        Map<Object, Object> partialParking = new HashMap<>(){{put("exitDate",LocalDateTime.now());}};
        Parking parking = parkingDB.get(id);
        if(parking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,parkingNotFound);
        }
        if(parking.getExitDate() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,operationAlreadyDone);
        }
        parking.getEntryDate().compareTo(LocalDateTime.now());
        Long min = ChronoUnit.MINUTES.between(parking.getEntryDate(),LocalDateTime.now());
        Number amount = 0;
        if(min >= 15) {
            amount = 0;
        }
        if(min >= 60) {
            amount = 5;
        }
        if(min >= 120) {
            amount = 8;
        }
        if (min > 180) {
            amount = 20;
        }
        patch(partialParking,id);
        String minutes = Helper.minutesToHoursMinutes(min);
        return new ExitCalTime(minutes,amount);
    }
}
