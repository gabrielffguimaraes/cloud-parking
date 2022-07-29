package one.digitalinnovation.parking.service.impl;

import one.digitalinnovation.parking.helpers.Helper;
import one.digitalinnovation.parking.model.Parking;
import one.digitalinnovation.parking.model.dto.ExitCalTime;
import one.digitalinnovation.parking.model.dto.ParkingCreateDTO;
import one.digitalinnovation.parking.model.dto.ParkingDTO;
import one.digitalinnovation.parking.model.entity.ParkingEntity;
import one.digitalinnovation.parking.repository.ParkingRepository;
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
    private final ParkingRepository parkingRepository;
    private final Helper helper;
    public ParkingServiceImp(ParkingRepository parkingRepository, Helper helper) {
        this.parkingRepository = parkingRepository;
        this.helper = helper;
    }
    @Value("${messages.parking-not-found}")
    private String parkingNotFound;
    @Value("${messages.operation-already-done}")
    private String operationAlreadyDone;
    @Value("${messages.id-invalido}")
    private String idInvalido;

    @Override
    public List<ParkingDTO> findAll() {
        List<ParkingEntity> parkingList = parkingRepository.findAll();
        List<ParkingDTO> result = helper.mapList(parkingList,ParkingDTO.class);
        return result;
    }


    @Override
    public ParkingEntity findById(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,idInvalido);
        }
        ParkingEntity parking = parkingRepository.findById(uuid)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,parkingNotFound));
        return parking;
    }
    @Override
    public ParkingDTO create(ParkingCreateDTO parkingCreate) {
        ParkingEntity parking = helper.map(parkingCreate,ParkingEntity.class);
        parkingRepository.save(parking);
        return helper.map(parking,ParkingDTO.class);
    }

    @Override
    public void deleteById(String id) {
        ParkingEntity parkingEntity = findById(id);
        parkingRepository.delete(parkingEntity);
    }
    @Override
    public ParkingDTO update(ParkingCreateDTO p,String id) {
        ParkingEntity parking = findById(id);
        parking.setColor(p.getColor());
        parking.setLicense(p.getLicense());
        parking.setModel(p.getModel());
        parking.setState(p.getState());
        parkingRepository.save(parking);
        return helper.map(parking,ParkingDTO.class);
    }
    @Override
    public ParkingDTO patch(Map<Object,Object> objectMap,String id) {
        ParkingEntity parking = findById(id);
        objectMap.forEach((key , value) -> {
            if(ReflectionUtils.findField(ParkingDTO.class,(String) key) != null) {
                Field field = ReflectionUtils.findField(ParkingEntity.class,(String) key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, parking, value);
            }
        });
        parkingRepository.save(parking);
        return helper.map(parking,ParkingDTO.class);
    }

    public ExitCalTime exit(String id) {
        ParkingEntity parking = findById(id);
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
        Map<Object, Object> partialParking = new HashMap<>(){{put("exitDate",LocalDateTime.now());}};
        patch(partialParking,id);
        String minutes = Helper.minutesToHoursMinutes(min);
        return new ExitCalTime(minutes,amount);
    }
}
