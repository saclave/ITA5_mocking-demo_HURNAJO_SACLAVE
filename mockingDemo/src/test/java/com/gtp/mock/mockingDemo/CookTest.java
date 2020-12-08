package com.gtp.mock.mockingDemo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

class CookTest {

    private ArrayList<String> order = new ArrayList();
    private Cook cook;
    private PrintStream save_out=System.out;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();


    @BeforeEach void setUp(){
        cook = mock(Cook.class);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void test_when_orders_are_added_cook_will_read_given_orders() {
        order.addAll(Arrays.asList("Adobo", "Fried Chicken"));
        cook.readOrder(order);

        verify(cook, atLeastOnce()).readOrder(any());
        doNothing().when(cook).readOrder(order);
    }

    @Test
    public void test_when_orders_are_added_cook_will_read_given_orders2() {
        Cook cook = new Cook();
        cook.readOrder(Collections.singletonList("Adobo"));
        assertEquals("Order has been added to queue\r\n", out.toString());
    }

    @Test
    public void test_when_cook_will_execute_cookOrder() throws InterruptedException{
        doNothing().when(cook).cookOrder("Adobo");
        cook.cookOrder("Adobo");

        verify(cook, atLeastOnce()).cookOrder(any());
    }

    @Test
    public void test_when_cook_is_busy_return_queue_not_empty(){
        Cook cook = new Cook(Arrays.asList("Adobo", "Fried Chicken"));
        assertTrue(cook.isBusy());
    }

    @Test
    public void test_when_cook_is_busy_return_queue_is_empty(){
        Cook cook = new Cook();
        assertFalse(cook.isBusy());
    }

    @Test
    public void test_when_cook_plates_order_will_return_null_when_orders_are_empty(){
        Cook cook = new Cook();
        assertNull(cook.plateOrder());
    }

    @Test
    public void test_when_cook_will_plate_order_given_orders_return_first_order() throws InterruptedException{
        Cook cook = new Cook(Arrays.asList("Adobo", "Fried Chicken"));
        cook.cookSingleOrder(singletonList("Adobo"));

        assertEquals("Adobo", cook.plateOrder().get(0));
    }

    @Test
    public void test_when_cook_will_cook_all_orders() throws InterruptedException {
        Cook cook = new Cook(Arrays.asList("Adobo"));
        cook.cookAllOrders();

        assertEquals("Cooking Adobo\r\nAdobo is/are cooked and ready\r\n", out.toString());
    }
}
