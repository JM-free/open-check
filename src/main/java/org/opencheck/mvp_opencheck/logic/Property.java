package org.opencheck.mvp_opencheck.logic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Represents the property with its information and state.
 *
 * @author  José Manuel & Edward L.
 * @version 1.0
 * @since   May 2022
 */

@Data
@AllArgsConstructor
public class Property {

    private String id;
    private String name;
    private String owner;
    private String email;
    private String phone;

    private Status status;
    private Address address;

    private List<Bill> extraActivities;
}
