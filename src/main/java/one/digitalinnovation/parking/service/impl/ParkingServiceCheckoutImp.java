package one.digitalinnovation.parking.service.impl;


import one.digitalinnovation.parking.model.entity.ParkingEntity;
import one.digitalinnovation.parking.service.ParkingServiceCheckout;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ParkingServiceCheckoutImp implements ParkingServiceCheckout {
    private final int HOUR = 60;
    private final int DAY = 24 * HOUR;
    private final double HOUR_VALUE = 5.00;
    private final double ADDITIONAL_PER_HOUR_VALUE = 2.00;
    private final double DAY_VALUE = 20.00;

    public double getBill(ParkingEntity parking) {
        return getBill(parking.getEntryDate(),parking.getExitDate());
    }
    private double getBill(LocalDateTime entryDate,LocalDateTime exitDate) {
        long minutes = ChronoUnit.MINUTES.between(entryDate,exitDate);
        double bill = 0.0;

        if(minutes <= HOUR) {
            bill = HOUR_VALUE;
        } else if(minutes < DAY) {
            bill = HOUR_VALUE;
            int hrs = (int) (minutes/HOUR) - 1;
            if(hrs > 0) {
                bill += hrs * ADDITIONAL_PER_HOUR_VALUE;
            }
        } else if(minutes >= DAY) {
            bill = DAY_VALUE;
            int days = (int) (minutes / DAY) - 1;
            int hrs = (int) (minutes / 60) % (DAY / 60);
            System.out.println(minutes);
            System.out.println(DAY);
            System.out.println(hrs);
            if (days > 0) {
                bill += days * DAY_VALUE;
                System.out.println(hrs);
                System.out.println(bill);
            }
            if(hrs > 0) {
                bill += hrs * ADDITIONAL_PER_HOUR_VALUE;
            }
        }
        return bill;
    }
}
