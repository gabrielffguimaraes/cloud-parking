package one.digitalinnovation.parking.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "parking")
@Data
public class ParkingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name="licenca")
    private String license;
    @Column(name="estado")
    private String state;
    @Column(name="modelo")
    private String model;
    @Column(name="cor")
    private String color;
    @Column(name = "crido_em")
    private LocalDateTime entryDate;
    @Column(name = "deixado_em")
    private LocalDateTime exitDate;
    @Column(name = "valor_a_pagar")
    private Double bill;

    @PrePersist
    private void prePersist() {
        this.setEntryDate(LocalDateTime.now());
    }
}
