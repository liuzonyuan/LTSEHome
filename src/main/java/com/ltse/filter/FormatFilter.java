package com.ltse.filter;

import com.ltse.dao.Order;

/**
 * filter for format, to filter out null orders or orders with invalid format
 * Part of the validation actually happens in #Order.buildOrder(String line)
 *
 * FILTER COVER:
 * 1. if the length of the fields not match
 * 2. if any value of the field doesn't match the type of that field
 * 3. if any field value is empty
 *
 * @author peterliu
 */
public class FormatFilter implements Filter{
    @Override
    public boolean applyFilter(Order order) {
        if (order == null)
            return false;
        if (order.getBroker() == null ||
                order.getBroker().isEmpty() ||
                order.getId() == 0 ||
                order.getSymbol() == null ||
                order.getSymbol().isEmpty() ||
                order.getType() == null ||
                order.getType().isEmpty() ||
                order.getQuantity() == 0 ||
                order.getPrice() == 0.0 ||
                order.getSide() == null
        ) {
            return false;
        }
        return true;
    }
}
