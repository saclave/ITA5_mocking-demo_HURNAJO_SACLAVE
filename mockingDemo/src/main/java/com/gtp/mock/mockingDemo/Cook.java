package com.gtp.mock.mockingDemo;

import java.util.ArrayList;
import static java.util.Collections.singletonList;

import java.util.Iterator;
import java.util.List;

public class Cook {

    private List<List<String>> queuedOrders;
    private List<List<String>> completedOrders;

    public Cook() {
        queuedOrders = new ArrayList<>();
        completedOrders = new ArrayList<>();
    }

    public Cook(List<String> orders) {
        queuedOrders = new ArrayList<>();
        queuedOrders.addAll(singletonList(orders));
        completedOrders = new ArrayList<>();
    }

    public void readOrder(List<String> currentOrders) {
        queuedOrders.add(currentOrders);
        System.out.println("Order has been added to queue");
    }

    public boolean isBusy(){
        return !queuedOrders.isEmpty() ;
    }

    public List<String> plateOrder() {
        if(completedOrders.isEmpty()) {
            System.out.println("All orders are not ready yet. Please wait");
            return null;
        }
        try {
            return completedOrders.get(0);
        } finally {
            completedOrders.remove(0);
        }

    }

    public void cookOrder(String order) throws InterruptedException {
        System.out.println("Cooking " + order);
        Thread.sleep(1500L);
    }

    public void cookSingleOrder(List<String> orders) throws InterruptedException {
        for (String order : orders) {
            cookOrder(order);
        }
        queuedOrders.remove(orders);
        completedOrders.add(orders);
        System.out.println(String.join(",", orders) + " is/are cooked and ready");
    }

    public void cookAllOrders() throws InterruptedException {
        Iterator<List<String>> iterator = queuedOrders.iterator();
        while (iterator.hasNext()) {
            List<String> orders = iterator.next();
            for (String order : orders) {
                cookOrder(order);
            }
            iterator.remove();
            completedOrders.add(orders);
            System.out.println(String.join(",", orders) + " is/are cooked and ready");
        }
    }

}
