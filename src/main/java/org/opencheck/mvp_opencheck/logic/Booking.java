package org.opencheck.mvp_opencheck.logic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public abstract class Booking {

    private String id;

    private Guest guest;
    private List<Room> rooms;
    private LocalDateTime dateIn;
    private LocalDateTime dateOut;

    private double finalBill;
    private Bill extraActivities;
    private boolean paymentState;

    public Booking(Guest guest, LocalDateTime dateIn, LocalDateTime dateOut, List<Room> rooms, Bill extraActivities) {
        this.guest = guest;
        this.rooms = rooms;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
        this.extraActivities = extraActivities;

        this.finalBill = calculateFinalBill();
    }

    private double calculateFinalBill(){
        double finalBill = 0.0;
        for (Room room : this.rooms){
            finalBill += room.getFinalPrice();
        }

        finalBill += extraActivities.getFinalAmount();

        return finalBill;
    }

    public void updateBooking(){
        this.finalBill = calculateFinalBill();
    }

    //APIs-related Methods
    abstract boolean validateBooking();
    abstract void makeBook(String roomType, LocalDateTime dateIn, LocalDateTime dateOut);

    //Class Method
    abstract List<String> msgBookingInfo();
    abstract Bill makeFinalBill(List<Bill> bills);
}
