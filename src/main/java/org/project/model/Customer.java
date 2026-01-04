package org.project.model;

import java.util.EnumMap;

public class Customer {
    private final String name;
    private final EnumMap<ProductCategory, Integer> basket;

    public Customer (String name, EnumMap<ProductCategory, Integer> basket) {
        this.name = name;
        this.basket = basket;
    }

    public EnumMap<ProductCategory, Integer> getBasket() {
        return basket;
    }

    @Override
    public String toString() {
        return "Customer{" + "name=" + name + ", basket=" + basket + '}';
    }
}
