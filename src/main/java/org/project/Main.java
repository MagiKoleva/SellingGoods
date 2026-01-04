package org.project;

import org.project.core.Store;
import org.project.model.Customer;
import org.project.model.ProductCategory;
import org.project.model.PurchaseResult;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        // Initial stock
        EnumMap<ProductCategory, Integer> stock = new EnumMap<>(ProductCategory.class);
        stock.put(ProductCategory.BREAD, 50);
        stock.put(ProductCategory.MILK, 40);
        stock.put(ProductCategory.CHEESE, 35);
        stock.put(ProductCategory.MEAT, 55);
        stock.put(ProductCategory.BEVERAGE, 70);

        Store store = new Store(stock);

        List<Customer> customers = generateCustomers(10);

        ExecutorService pool = Executors.newFixedThreadPool(4);
        List<Future<PurchaseResult>> futures = new ArrayList<>();

        for (Customer c : customers) {
            futures.add(pool.submit(() -> {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 400));
                PurchaseResult result = store.sellBasket(c.getBasket());
                System.out.println(c);
                System.out.println("     RESULT -> " + result);
                return result;
            }));
        }

        for (Future<PurchaseResult> f : futures) {
            try {
                f.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println();
        System.out.println("---FINAL STORE STOCK---");
        store.getStockSnapshot()
                .forEach((k, v) -> System.out.println(k + " -> " + v));

    }

    private static List<Customer> generateCustomers(int number) {
        Random r = new Random();
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i <= number; i++) {
            EnumMap<ProductCategory, Integer> basket = new EnumMap<>(ProductCategory.class);
            basket.put(ProductCategory.BREAD, r.nextInt(1, 5));
            basket.put(ProductCategory.MEAT, r.nextInt(0, 10));
            basket.put(ProductCategory.MILK, r.nextInt(0, 4));
            basket.put(ProductCategory.CHEESE, r.nextInt(0, 4));
            basket.put(ProductCategory.BEVERAGE, r.nextInt(1, 10));

            customers.add(new Customer("Customer " + i + ": ", basket));
        }
        return customers;
    }
}
