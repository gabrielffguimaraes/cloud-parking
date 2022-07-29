package one.digitalinnovation.parking.controller;

import io.swagger.v3.oas.annotations.OpenAPI30;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import one.digitalinnovation.parking.model.Parking;
import one.digitalinnovation.parking.model.dto.ParkingCreateDTO;
import one.digitalinnovation.parking.model.dto.ParkingDTO;
import one.digitalinnovation.parking.service.ParkingService;
import one.digitalinnovation.parking.service.impl.ParkingServiceImp;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parking")
public class ParkingController {
    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping
    public ResponseEntity<List<ParkingDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkingService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<ParkingDTO> findById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkingService.findById(id));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        this.parkingService.deleteById(id);
    }

    @PostMapping
    public ResponseEntity<ParkingDTO> create(@RequestBody ParkingCreateDTO parkingDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.parkingService.create(parkingDTO));
    }

    @PutMapping("{id}")
    public ResponseEntity<ParkingDTO> update(@RequestBody ParkingCreateDTO parkingDTO,@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkingService.update(parkingDTO,id));
    }

    @PatchMapping("{id}")
    public void exit(@PathVariable String id) {
        this.parkingService.exit(id);
    }
}
