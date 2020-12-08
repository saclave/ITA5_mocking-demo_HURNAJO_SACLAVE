package com.gtp.mock.mockingDemo;

import java.util.ArrayList;
import java.util.List;

public class Waiter {

    private final List<String> currentOrders;
    private final Cook cook;

    public Waiter(List<String> orders, Cook cook){
        currentOrders = new ArrayList<>();
        if(orders != null) {
            currentOrders.addAll(orders);
        }
        this.cook = cook;
    }

    public String takeOrder(String order){
        currentOrders.add(order);
        System.out.println("Took order " + order);
        return "Took order " + order;
    }

    public String repeatOrder(){
        return currentOrders.isEmpty() ? "You haven't ordered anything yet."
                : "Your current order/s :" + String.join(",", currentOrders);
    }

    public void relayOrder() throws InterruptedException {
        if (currentOrders.isEmpty()){
            System.out.println("You haven't ordered anything yet.");
            return;
        }
        cook.readOrder(currentOrders);
        cook.cookSingleOrder(currentOrders);
        currentOrders.clear();
        System.out.println("Relayed orders to cook. Waiter can now take more orders");
    }

    public String serveOrder() {
        if(cook.isBusy()) {
            System.out.println("All orders are not ready yet. Please wait");
            return "All orders are not ready yet. Please wait";
        }
        List<String> completedOrder = cook.plateOrder();
        System.out.println("Now serving:" + String.join(",", completedOrder));
        return "Now serving:" + String.join(",", completedOrder);
    }

    public void pressureCook() {
        try {
            cook.cookAllOrders();
        } catch (InterruptedException e) {
            System.out.println("Cook has failed to cook");
        }
    }
}
