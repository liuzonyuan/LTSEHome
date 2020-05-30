package com.ltse.dao;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class OrderTest {
    private Order order;

    @Before
    public void setup() {
        order = new Order()
                .setTimeStamp(1483628400000l)
                .setBroker("Fidelity")
                .setId(1)
                .setSymbol("BARK")
                .setQuantity(100)
                .setPrice(1.195)
                .setSide(Order.SIDE.Buy)
                .setType("2");
    }

    @Test
    public void testBuildOrder() {
        String line = "10/5/2017 10:00:00,Fidelity,1,2,BARK,100,1.195,Buy";
        assertTrue(order.equals(Order.buildOrder(line)));
    }

    @Test
    public void testToSimpleLine() {
        String line = Order.toSimpleLine(order);
        assertTrue(line.equals("Fidelity,1"));
    }
}
