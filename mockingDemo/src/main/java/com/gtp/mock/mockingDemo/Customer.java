package com.gtp.mock.mockingDemo;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private Waiter waiter;
    private List<String> orderList = new ArrayList<>();

    public Customer(Waiter waiter){
        this.waiter = waiter;
    }

    public void setOrderList(List<String> orders){
        this.orderList = orders;
    }

    public String give(List<String> orders){
        for(String order : orders){
            waiter.takeOrder(order);
        }
        setOrderList(orders);
        return "Your current order/s: " + String.join(",", orderList);
    }

    public String confirm(){
        return waiter.repeatOrder();
    }

    public void followUp(){
        waiter.pressureCook();
    }

    public String eat(List<String> orders){
        String order = waiter.serveOrder();
        if(order.contains("Now serving")){
            orderList.removeAll(orders);
            return "Thanks for the meal.";
        }
        else {
            return "Still waiting for my food.";
        }
    }
}
