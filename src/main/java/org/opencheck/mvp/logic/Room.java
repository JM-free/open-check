package org.opencheck.mvp.logic;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class Room {
    private String id;
    private String name;
    private String type;
    private String description;

    private double basePrice;
    private Bill extraItems;
    private double finalPrice;


    public Room(String id, String name, String type, String description, double basePrice) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.basePrice = basePrice;
        this.description = description;
        
        this.finalPrice = this.basePrice;
    }

    public Room(String id, String name, String type, String description, double basePrice,
                @NotNull Bill extraItems) {

        this.id = id;
        this.name = name;
        this.type = type;
        this.basePrice = basePrice;
        this.description = description;
        this.extraItems = extraItems;
        this.finalPrice = extraItems.getFinalAmount();
    }

    public Bill addExtraItem(Bill newExtraItem){
        this.extraItems.addNewBill(newExtraItem);
        return this.extraItems;
    }
}
