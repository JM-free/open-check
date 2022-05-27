package org.opencheck.mvp_opencheck.logic;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Bill {
    private Map<String, Double> conceptsAndPrices;
    private double finalAmount;

    protected Bill(Map<String, Double> conceptsAndPrices) {
        this.conceptsAndPrices = conceptsAndPrices;
        this.finalAmount = calculateFinalAmount(conceptsAndPrices);
    }

    public double calculateFinalAmount(@NotNull Map<String, Double> bill){
        final long[] i = {0};
        bill.forEach((k, v) -> i[0] += v);
        return i[0];
    }

    public void addNewBill(@NotNull Bill newBill){
        Map<String, Double> newMap = new HashMap<>(this.conceptsAndPrices);
        newBill.getConceptsAndPrices().forEach((key, value) -> newMap.merge(key, value, Double::sum));

        this.conceptsAndPrices = newMap;
        this.finalAmount += newBill.getFinalAmount();
    }

}
