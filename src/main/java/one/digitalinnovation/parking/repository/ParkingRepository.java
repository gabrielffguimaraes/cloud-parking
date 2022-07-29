package one.digitalinnovation.parking.repository;

import one.digitalinnovation.parking.model.entity.ParkingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingEntity, UUID> {
    List<ParkingEntity> findAll();
    Optional<ParkingEntity> findById(UUID id);

    ParkingEntity save(ParkingEntity parking);

    void delete(ParkingEntity parking);
}
