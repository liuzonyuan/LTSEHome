package com.ltse.filter;

import com.ltse.dao.Order;

import java.util.*;

/**
 * WINDOW FILTER
 * Each broker may only submit three orders per minute: any additional orders in should be
 * rejected
 *
 * there are two implementations:
 * rolling window will start from the first of the three last orders and decide accept or reject
 * fixed window will start from the beginning of the minute and decide accept or reject
 */
public class WindowFilter implements Filter {
    private Map<String, Queue<Long>> map = new HashMap<>();
    public static String mode = "rolling";
    @Override
    public boolean applyFilter(Order order) {
        if (mode.equals("rolling")) {
            return rollingWindow(order);
        } else {
            return fixedWindow(order);
        }
    }

    private boolean fixedWindow(Order order) {
        long ts = order.getTimestamp();
        Queue<Long> q = map.computeIfAbsent(order.getSymbol(), s->new LinkedList<Long>());
        if(q.size() < 3) {
            q.offer(ts);
            return true;
        } else {
            int currentMinute = new Date(ts).getMinutes();
            while(!q.isEmpty()) {
                int lastMinute = new Date(q.peek()).getMinutes();
                if(lastMinute != currentMinute) {
                    q.poll();
                } else {
                    break;
                }
            }
            if(q.size() >= 3) {
                return false;
            }else {
                q.offer(ts);
                return true;
            }
        }
    }

    private boolean rollingWindow(Order order) {
        long ts = order.getTimestamp();
        Queue<Long> q = map.computeIfAbsent(order.getBroker(), s->new LinkedList<>());
        if(q.size() < 3) {
            q.offer(ts);
            return true;
        } else {
            if (ts - q.peek() <= 60000) {
                return false;
            } else {
                q.poll();
                q.offer(ts);
                return true;
            }
        }
    }
}
