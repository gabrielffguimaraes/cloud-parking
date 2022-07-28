package one.digitalinnovation.parking.controller;

import one.digitalinnovation.parking.model.Parking;
import one.digitalinnovation.parking.model.dto.ParkingDTO;
import one.digitalinnovation.parking.service.ParkingService;
import one.digitalinnovation.parking.service.impl.ParkingServiceImp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/parking")
public class ParkingController {
    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping
    public List<ParkingDTO> findAll() {
        return this.parkingService.findAll();
    }
}
