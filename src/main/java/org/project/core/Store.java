package org.project.core;

import org.project.model.ProductCategory;
import org.project.model.PurchaseResult;

import java.util.EnumMap;
import java.util.Map;

public class Store {
    private final EnumMap<ProductCategory, Integer> stock;

    public Store(EnumMap<ProductCategory, Integer> initialStock) {
        this.stock = initialStock;
    }

    public synchronized PurchaseResult sellBasket(EnumMap<ProductCategory, Integer> basket) {
        // check if there are enough products in stock
        for (Map.Entry<ProductCategory, Integer> e : basket.entrySet()) {
            int available = stock.getOrDefault(e.getKey(), 0);
            if (available < e.getValue()) {
                return new PurchaseResult(false, new EnumMap<>(ProductCategory.class));
            }
        }

        // if there are enough products we decrease the stock
        EnumMap<ProductCategory, Integer> sold = new EnumMap<>(ProductCategory.class);
        for (Map.Entry<ProductCategory, Integer> e : basket.entrySet()) {
            stock.put(e.getKey(), stock.get(e.getKey()) - e.getValue());
            sold.put(e.getKey(), e.getValue());
        }

        return new PurchaseResult(true, sold);
    }

    public synchronized EnumMap<ProductCategory, Integer> getStockSnapshot() {
        return new EnumMap<>(stock);
    }
}
