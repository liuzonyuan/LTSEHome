package com.ltse.filter;

import com.ltse.dao.Order;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class FilterTest {
    Filter formatFilter, symbolFilter, windowFilter, uniqueFilter;

    @Before
    public void setup() {
        formatFilter = new FormatFilter();
        symbolFilter = new SymbolFilter();
        windowFilter = new WindowFilter();
        uniqueFilter = new UniqueFilter();
    }

    @Test
    public void testFormatFilterWithNullOrder() {
        String line = "";
        Order order = Order.buildOrder(line);
        assertFalse(formatFilter.applyFilter(order));
    }

    @Test
    public void testFormatFilterWithNullField() {
        String line = "10/5/2017 10:00:00,Fidelity,1,2,,100,1.195,Buy";
        Order order = Order.buildOrder(line);
        assertFalse(formatFilter.applyFilter(order));
    }

    @Test
    public void testFormatFilterWithValidLine() {
        String line = "10/5/2017 10:00:00,Fidelity,1,2,BARK,100,1.195,Buy";
        Order order = Order.buildOrder(line);
        assertTrue(formatFilter.applyFilter(order));
    }

    @Test
    public void testSymbolFilterWithInValidSymbol() {
        String line = "10/5/2017 10:00:00,Fidelity,1,2,BANK,100,1.195,Buy";
        Order order = Order.buildOrder(line);
        assertFalse(symbolFilter.applyFilter(order));
    }

    @Test
    public void testSymbolFilterWithValidSymbol() {
        String line = "10/5/2017 10:00:00,Fidelity,1,2,BARK,100,1.195,Buy";
        Order order = Order.buildOrder(line);
        assertTrue(symbolFilter.applyFilter(order));
    }

    @Test
    public void testWindowFilterWithRollingWindow() {
        String line = "10/5/2017 10:00:00,Fidelity,1,2,BARK,100,1.195,Buy";
        Order order = Order.buildOrder(line);
        assertTrue(windowFilter.applyFilter(order));
        line = "10/5/2017 10:00:05,Fidelity,2,2,BARK,100,1.195,Buy";
        order = Order.buildOrder(line);
        assertTrue(windowFilter.applyFilter(order));
        line = "10/5/2017 10:00:10,Fidelity,3,2,BARK,100,1.195,Buy";
        order = Order.buildOrder(line);
        assertTrue(windowFilter.applyFilter(order));
        line = "10/5/2017 10:00:15,Fidelity,4,2,BARK,100,1.195,Buy";
        order = Order.buildOrder(line);
        assertFalse(windowFilter.applyFilter(order));
        line = "10/5/2017 10:01:15,Fidelity,4,2,BARK,100,1.195,Buy";
        order = Order.buildOrder(line);
        assertTrue(windowFilter.applyFilter(order));
    }

    @Test
    public void testWindowFilterWithFixedWindow() {
        ((WindowFilter)windowFilter).mode = "fixed";
        String line = "10/5/2017 10:00:40,Fidelity,1,2,BARK,100,1.195,Buy";
        Order order = Order.buildOrder(line);
        assertTrue(windowFilter.applyFilter(order));
        line = "10/5/2017 10:00:45,Fidelity,2,2,BARK,100,1.195,Buy";
        order = Order.buildOrder(line);
        assertTrue(windowFilter.applyFilter(order));
        line = "10/5/2017 10:00:50,Fidelity,3,2,BARK,100,1.195,Buy";
        order = Order.buildOrder(line);
        assertTrue(windowFilter.applyFilter(order));
        line = "10/5/2017 10:00:55,Fidelity,4,2,BARK,100,1.195,Buy";
        order = Order.buildOrder(line);
        assertFalse(windowFilter.applyFilter(order));
        line = "10/5/2017 10:01:05,Fidelity,5,2,BARK,100,1.195,Buy";
        order = Order.buildOrder(line);
        assertTrue(windowFilter.applyFilter(order));
        line = "10/5/2017 10:01:15,Fidelity,6,2,BARK,100,1.195,Buy";
        order = Order.buildOrder(line);
        assertTrue(windowFilter.applyFilter(order));
    }

    @Test
    public void testUniqueFilter() {
        String line = "10/5/2017 10:00:00,Fidelity,1,2,BARK,100,1.195,Buy";
        Order order = Order.buildOrder(line);
        assertTrue(uniqueFilter.applyFilter(order));
        line = "10/5/2017 10:00:05,Fidelity,1,2,BARK,100,1.195,Buy";
        order = Order.buildOrder(line);
        assertFalse(uniqueFilter.applyFilter(order));
        line = "10/5/2017 10:00:10,Fidelity,2,2,BARK,100,1.195,Buy";
        order = Order.buildOrder(line);
        assertTrue(uniqueFilter.applyFilter(order));
    }
}
