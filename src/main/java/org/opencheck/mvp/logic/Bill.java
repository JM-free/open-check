package org.opencheck.mvp.logic;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Bill {
    private Map<String, Double> bills;
    private double finalAmount;

    protected Bill(Map<String, Double> bills) {
        this.bills = bills;
        this.finalAmount = calculateFinalAmount(bills);
    }

    public double calculateFinalAmount(@NotNull Map<String, Double> bill){
        final long[] i = {0};
        bill.forEach((k, v) -> i[0] += v);
        return i[0];
    }

    public Bill addNewBill(@NotNull Bill newBill){
        Map<String, Double> newMap = new HashMap<>(this.bills);
        newBill.getBills().forEach((key, value) -> newMap.merge(key, value, (value1, value2) -> value1 + value2));

        this.bills = newMap;
        this.finalAmount += newBill.getFinalAmount();

        return this;
    }

}
