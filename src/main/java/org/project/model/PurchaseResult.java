package org.project.model;

import java.util.EnumMap;

public class PurchaseResult {
    private final boolean success;
    private final EnumMap<ProductCategory, Integer> sold;

    public PurchaseResult(boolean success, EnumMap<ProductCategory, Integer> sold) {
        this.success = success;
        this.sold = sold;
    }

    public boolean isSuccess() {
        return success;
    }

    public EnumMap<ProductCategory, Integer> getSoldProducts() {
        return sold;
    }

    @Override
    public String toString() {
        return success
                ? "SUCCESS: sold= " + sold
                : "FAILED (not enough stock)";
    }
}
