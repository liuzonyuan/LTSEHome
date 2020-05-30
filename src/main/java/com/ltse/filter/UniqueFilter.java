package com.ltse.filter;

import com.ltse.dao.Order;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * UNIQUE FILTER
 * Within a single broker’s trades ids must be unique. If ids repeat for the same broker, only the
 * first message with a given id should be accepted.
 *
 * @author peterliu
 */
public class UniqueFilter implements Filter {
    private Map<String, Set<Long>> orderIdMap = new HashMap<>();

    @Override
    public boolean applyFilter(Order order) {
        return orderIdMap.computeIfAbsent(order.getBroker(), x->new HashSet()).add(order.getId());
    }
}
