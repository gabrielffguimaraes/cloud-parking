package one.digitalinnovation.parking.service.impl;

import one.digitalinnovation.parking.helpers.Helper;
import one.digitalinnovation.parking.model.dto.ParkingCreateDTO;
import one.digitalinnovation.parking.model.dto.ParkingDTO;
import one.digitalinnovation.parking.model.entity.ParkingEntity;
import one.digitalinnovation.parking.repository.ParkingRepository;
import one.digitalinnovation.parking.service.ParkingService;
import one.digitalinnovation.parking.service.ParkingServiceCheckout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;


import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class ParkingServiceImp implements ParkingService {
    private final ParkingRepository parkingRepository;
    private final ParkingServiceCheckout parkingServiceCheckout;
    private final Helper helper;
    public ParkingServiceImp(ParkingRepository parkingRepository, ParkingServiceCheckout parkingServiceCheckout, Helper helper) {
        this.parkingRepository = parkingRepository;
        this.parkingServiceCheckout = parkingServiceCheckout;
        this.helper = helper;
    }
    @Value("${messages.parking-not-found}")
    private String parkingNotFound;
    @Value("${messages.operation-already-done}")
    private String operationAlreadyDone;
    @Value("${messages.id-invalido}")
    private String idInvalido;

    @Override
    @Transactional(readOnly = true)
    public List<ParkingDTO> findAll() {
        List<ParkingEntity> parkingList = parkingRepository.findAll();
        List<ParkingDTO> result = helper.mapList(parkingList,ParkingDTO.class);
        return result;
    }


    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
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
    @Transactional
    public ParkingDTO create(ParkingCreateDTO parkingCreate) {
        ParkingEntity parking = helper.map(parkingCreate,ParkingEntity.class);
        parkingRepository.save(parking);
        return helper.map(parking,ParkingDTO.class);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        ParkingEntity parkingEntity = findById(id);
        parkingRepository.delete(parkingEntity);
    }
    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
    public ParkingDTO exit(String id) {
        ParkingEntity parking = findById(id);
        if(parking.getExitDate() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,operationAlreadyDone);
        }
        parking.setExitDate(LocalDateTime.now());
        double bill = parkingServiceCheckout.getBill(parking);

        Map<Object, Object> partialParking = new HashMap<>(){{
            put("exitDate",LocalDateTime.now());
            put("bill",bill);
        }};
        return patch(partialParking,id);
    }
}
