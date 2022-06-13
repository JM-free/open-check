package org.opencheck.mvp_opencheck.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Bill {
    private Map<String, Double> bill;
    private double finalAmount;

    protected Bill(Map<String, Double> bill) {
        this.bill = bill;
        this.finalAmount = calculateFinalAmount(bill);
    }

    public double calculateFinalAmount(@NotNull Map<String, Double> bill){
        final long[] i = {0};
        bill.forEach((k, v) -> i[0] += v);
        return i[0];
    }

    public Bill addNewBill(@NotNull Bill newBill){
        Map<String, Double> newMap = new HashMap<>(this.bill);
        newBill.getBill().forEach((key, value) -> newMap.merge(key, value, (value1, value2) -> value1 + value2));

        this.bill = newMap;
        this.finalAmount += newBill.getFinalAmount();

        return this;
    }

}
