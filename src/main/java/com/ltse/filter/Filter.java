package com.ltse.filter;

import com.ltse.dao.Order;

public interface Filter {
    boolean applyFilter(Order order);
}
