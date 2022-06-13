package org.opencheck.mvp_opencheck.logic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public abstract class Booking {

    //TODO check implementation.

    private String id;
    private Guest guest;
    private List<Room> rooms;

    private LocalDateTime dateIn;
    private LocalDateTime dateOut;

    private Map<String, Double> extraActivities;
    private double finalBill;
    private boolean paymentState = false;

    //APIs-related Methods
    abstract boolean validateBooking();

    abstract void makeBook(String roomType, LocalDateTime dateIn, LocalDateTime dateOut);

    //Class Method
    abstract List<String> msgBookingInfo();

    abstract Bill makeFinalBill(List<Bill> bills);
}
