package com.gtp.mock.mockingDemo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Arrays;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WaiterTest {

    private Waiter waiter;
    private PrintStream save_out=System.out;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @BeforeEach void setUp(){
        System.setOut(new PrintStream(out));
    }

    @Test
    public void takeOrder_should_take_one_order() {
        waiter = new Waiter(singletonList("Chickenjoy"), null);

        assertThat(waiter.takeOrder("Mango Shake")).isEqualTo("Took order Mango Shake");
    }

    @Test
    public void repeatOrder_should_repeat_orders() {
        waiter = new Waiter(Arrays.asList("Fries", "Chocolate Sundae"), null);
        waiter.takeOrder("Mango Shake");
        waiter.takeOrder("Chickenjoy");
        waiter.takeOrder("Extra Gravy");

        assertThat(waiter.repeatOrder()).isEqualTo("Your current order/s :"
                + "Fries,Chocolate Sundae,Mango Shake,Chickenjoy,Extra Gravy");
    }

    @Test
    void relayOrder_cook_should_read_order() throws InterruptedException {
        Cook cook = mock(Cook.class);
        Waiter waiter = new Waiter(singletonList("Orange"),cook);
        waiter.relayOrder();

        verify(cook, atLeastOnce()).readOrder(any());
    }

    @Test
    void serveOrder_waiter_should_serve_completed_order() {
        Cook cook =  mock(Cook.class);
        Waiter waiter = new Waiter(singletonList("Adobo"),cook);

        when(cook.isBusy()).thenReturn(false);
        doReturn(singletonList("Iced Tea")).when(cook).plateOrder();

        String serverOrder = waiter.serveOrder();

        verify(cook, atLeastOnce()).isBusy();
        verify(cook, atLeastOnce()).plateOrder();
        assertThat(serverOrder).isEqualTo("Now serving:Iced Tea");

    }

    @Test
    void pressureCook() throws InterruptedException {
        Cook cook =  mock(Cook.class);
        Waiter waiter = new Waiter(singletonList("Adobo"),cook);
        waiter.pressureCook();

        doNothing().when(cook).cookAllOrders();

        verify(cook,atLeastOnce()).cookAllOrders();
    }

    @Test
    void pressureCook_wait_cook_to_finish() throws InterruptedException {
        Cook realCook = new Cook(Arrays.asList("Rice","Chicken"));
        Cook spyCook = spy(realCook);

        Waiter waiter = new Waiter(singletonList("Ice"),spyCook);
        waiter.pressureCook();
        doNothing().when(spyCook).cookOrder(anyString());

        verify(spyCook,atLeastOnce()).cookAllOrders();
        verify(spyCook,times(2)).cookOrder(anyString());
    }

    @Test
    void test_when_cook_is_busy_waiter_will_tell_to_wait(){
        Cook cook = mock(Cook.class);
        when(cook.isBusy()).thenReturn(true);
        Waiter waiter = new Waiter(singletonList("Adobo"),cook);
        waiter.serveOrder();

        assertEquals("All orders are not ready yet. Please wait\r\n", out.toString());
    }

    @Test
    void test_when_waiter_relays_order_but_current_order_is_empty() throws InterruptedException {
        Cook cook = mock(Cook.class);
        Waiter waiter = new Waiter(null, cook);
        waiter.relayOrder();

        assertEquals("You haven't ordered anything yet.\r\n", out.toString());
    }

}