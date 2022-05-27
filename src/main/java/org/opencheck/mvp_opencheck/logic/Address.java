package org.opencheck.mvp_opencheck.logic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private String city;
    private String street;
    private String country;
    private String postCode;
}
