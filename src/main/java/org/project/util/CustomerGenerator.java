package org.project.util;

import org.project.model.Customer;
import org.project.model.ProductCategory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class CustomerGenerator {

    private static final Random RANDOM = new Random();
    private CustomerGenerator() {}

    public static List<Customer> generateCustomers(int number) {
        List<Customer> customers = new ArrayList<>();

        for (int i = 1; i <= number; i++) {
            EnumMap<ProductCategory, Integer> basket = generateBasket();

            customers.add(new Customer("Customer #" + i, basket));
        }
        return customers;
    }

    private static EnumMap<ProductCategory, Integer> generateBasket() {
        EnumMap<ProductCategory, Integer> basket = new EnumMap<>(ProductCategory.class);

        basket.put(ProductCategory.BREAD, RANDOM.nextInt(1, 5));
        basket.put(ProductCategory.MEAT, RANDOM.nextInt(0, 10));
        basket.put(ProductCategory.MILK, RANDOM.nextInt(0, 4));
        basket.put(ProductCategory.CHEESE, RANDOM.nextInt(0, 4));
        basket.put(ProductCategory.BEVERAGE, RANDOM.nextInt(1, 10));

        return basket;
    }
}
