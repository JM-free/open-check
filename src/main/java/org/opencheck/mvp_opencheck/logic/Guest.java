package org.opencheck.mvp_opencheck.logic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Guest {

    private String id;
    private String dni;
    private String name;
    private String lastName;

    private int phone;
    private String email;
    private Address address;

    private Bill bill;
    private Room room;
    private Booking booking;
}
