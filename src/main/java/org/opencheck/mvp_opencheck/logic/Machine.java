package org.opencheck.mvp_opencheck.logic;

public interface Machine {
    Guest makePayment(Guest guest);
    Key makeKey(Guest guest);
    boolean checkIn(String[] guestData, Booking guestBooking, String language);
    boolean checkOut(String[] guestData);
    boolean openMainDoor(boolean validGuest);
}
