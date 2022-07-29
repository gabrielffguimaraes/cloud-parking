package one.digitalinnovation.parking.service.impl;

import one.digitalinnovation.parking.helpers.Helper;
import one.digitalinnovation.parking.model.Parking;
import one.digitalinnovation.parking.model.dto.ParkingCreateDTO;
import one.digitalinnovation.parking.model.dto.ParkingDTO;
import one.digitalinnovation.parking.service.ParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Parking não encontrado");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Parking não encontrado");
        }
        parkingDB.remove(id);
    }
    @Override
    public ParkingDTO update(ParkingCreateDTO parkingDTO,String id) {
        Parking parking = parkingDB.get(id);
        if(parking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Parking não encontrado");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Parking não encontrado");
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

    public void exit(String id) {
        Map<Object, Object> partialParking = new HashMap<>(){{put("exitDate",LocalDateTime.now());}};
        patch(partialParking,id);
    }
}
