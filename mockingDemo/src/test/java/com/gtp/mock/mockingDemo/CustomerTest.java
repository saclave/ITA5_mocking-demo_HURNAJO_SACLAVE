package com.gtp.mock.mockingDemo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerTest {
    private Customer customer;
    private Waiter waiter;

    @BeforeEach
    void setUp(){
        waiter = mock(Waiter.class);
        customer = new Customer(waiter);
    }

    @Test
    public void test_when_customer_gives_order_return_order(){
        when(waiter.takeOrder(anyString())).thenReturn("Adobo");
        String result = customer.give(Collections.singletonList("Adobo"));

        assertThat(result).isEqualTo("Your current order/s: Adobo");
        verify(waiter, atLeastOnce()).takeOrder(any());
    }

    @Test
    public void test_when_customer_confirms_order_waiter_will_repeat_order(){
        when(waiter.takeOrder(anyString())).thenReturn("Adobo");
        String confirm = customer.give(Collections.singletonList("Adobo"));

        assertThat(confirm).isEqualTo("Your current order/s: Adobo");
    }

    @Test
    public void test_when_customer_confirm_order_but_null_order() {
        doReturn("You haven't ordered anything yet.").when(waiter).repeatOrder();
        String confirm = customer.confirm();

        assertThat(confirm).isEqualTo("You haven't ordered anything yet.");
        verify(waiter, atLeastOnce()).repeatOrder();
    }

    @Test
    public void test_when_customer_will_followup_order(){
        customer.followUp();
        doNothing().when(waiter).pressureCook();
        verify(waiter, atLeastOnce()).pressureCook();
    }

    @Test
    public void test_when_customer_eats_order_should_return_string() {
        when(waiter.serveOrder()).thenReturn("Now serving:Adobo");
        String order = customer.eat(Collections.singletonList("Adobo"));

        assertThat(order).isEqualTo("Thanks for the meal.");
        verify(waiter, atLeastOnce()).serveOrder();
    }

    @Test
    public void test_when_customer_eats_but_food_has_not_been_served() {
        when(waiter.serveOrder()).thenReturn("All orders are not ready yet. Please wait");
        String order = customer.eat(Collections.singletonList("Adobo"));

        assertThat(order).isEqualTo("Still waiting for my food.");
        verify(waiter, atLeastOnce()).serveOrder();
    }

}
