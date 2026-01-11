package org.project;

import org.project.core.Store;
import org.project.model.Customer;
import org.project.model.ProductCategory;
import org.project.model.PurchaseResult;
import org.project.util.CustomerGenerator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
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

        System.out.println("----- Initial store stock -----");
        printSnapshot(store);
        System.out.println();

        List<Customer> customers = CustomerGenerator.generateCustomers(10);

        ExecutorService pool = Executors.newFixedThreadPool(4);
        List<Future<PurchaseResult>> results = new ArrayList<>();

        for (Customer c : customers) {
            results.add(pool.submit(() -> {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 400));
                PurchaseResult result = store.sellBasket(c.getBasket());
                System.out.println(c);
                System.out.println("R E S U L T -> " + result);
                return result;
            }));
        }

        for (Future<PurchaseResult> f : results) {
            try {
                f.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);


        EnumMap<ProductCategory, Integer> totalPurchased =
                calculateTotalPurchased(results);

        printTotalPurchased(totalPurchased);


        System.out.println();
        System.out.println("----- Final store stock -----");
        printSnapshot(store);

    }

    private static EnumMap<ProductCategory, Integer> calculateTotalPurchased(
            List<Future<PurchaseResult>> futures) {

        EnumMap<ProductCategory, Integer> totalPurchased =
                new EnumMap<>(ProductCategory.class);

        for (Future<PurchaseResult> f : futures) {
            try {
                PurchaseResult result = f.get();

                if (result.isSuccess()) {
                    result.getSoldProducts().forEach((product, qty) ->
                            totalPurchased.merge(product, qty, Integer::sum));
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return totalPurchased;
    }

    private static void printTotalPurchased(
            EnumMap<ProductCategory, Integer> purchased) {

        System.out.println();
        System.out.println("--- TOTAL PRODUCTS PURCHASED ---");

        purchased.forEach((product, qty) ->
                System.out.println(product + " -> " + qty));
    }


    private static void printSnapshot(Store store) {
        store.getStockSnapshot()
                .forEach((k, v) -> System.out.println(k + " -> " + v));
    }
}
