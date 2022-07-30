package one.digitalinnovation.parking.controller;

import one.digitalinnovation.parking.helpers.Helper;
import one.digitalinnovation.parking.model.dto.ParkingCreateDTO;
import one.digitalinnovation.parking.model.dto.ParkingDTO;
import one.digitalinnovation.parking.model.entity.ParkingEntity;
import one.digitalinnovation.parking.service.ParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking")
public class ParkingController {
    private final Helper helper;
    private final ParkingService parkingService;

    public ParkingController(Helper helper, ParkingService parkingService) {
        this.helper = helper;
        this.parkingService = parkingService;
    }

    @GetMapping
    public ResponseEntity<List<ParkingDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkingService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<ParkingDTO> findById(@PathVariable String id) {
        ParkingEntity parkingEntity = this.parkingService.findById(id);
        ParkingDTO result = helper.map(parkingEntity,ParkingDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(result);
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
    public ResponseEntity<ParkingDTO> exit(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkingService.exit(id));
    }
}
