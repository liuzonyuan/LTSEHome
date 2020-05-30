package com.ltse.dao;

import com.ltse.protobuf.OrderOuterClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Order object
 * @author peterliu
 */
public class Order {
    private static final Logger LOGGER = LogManager.getLogger(Order.class);
    private static final String csvSplitBy = ",";
    private static final int ORDER_LENGTH = 8;

    private static final int timestampIdx = 0;
    private static final int brokerIdx = 1;
    private static final int orderIdx = 2;
    private static final int typeIdx = 3;
    private static final int symbolIdx = 4;
    private static final int quantityIdx = 5;
    private static final int priceIdx = 6;
    private static final int sideIdx = 7;

    private long timestamp;
    private String broker;
    private long id;
    private String type;
    private String symbol;
    private int quantity;
    private double price;
    private SIDE side;

    public enum SIDE {
        Buy, Sell
    }

    public Order () {}

    protected Order setTimeStamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    protected Order setBroker(String broker) {
        this.broker = broker;
        return this;
    }

    protected Order setId(long id) {
        this.id = id;
        return this;
    }

    protected Order setType(String type) {
        this.type = type;
        return this;
    }

    protected Order setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    protected Order setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    protected Order setPrice(double price) {
        this.price = price;
        return this;
    }

    protected Order setSide(SIDE side) {
        this.side = side;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getBroker() {
        return broker;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public SIDE getSide() {
        return side;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return timestamp == order.timestamp &&
                id == order.id &&
                quantity == order.quantity &&
                Double.compare(order.price, price) == 0 &&
                Objects.equals(broker, order.broker) &&
                Objects.equals(type, order.type) &&
                Objects.equals(symbol, order.symbol) &&
                side == order.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, broker, id, type, symbol, quantity, price, side);
    }

    // convert date string to timestamp
    private static long toTimeStamp(String dateInString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("mm/d/yyyy hh:mm:ss");
        Date date = formatter.parse(dateInString);
        return date.getTime();
    }

    // build the order from the input stream, return null if length is not correct
    // or value type is not correct
    public static Order buildOrder(String line) {
        String[] row = line.split(csvSplitBy);
        if(row.length != 8) {
            return null;
        }
        try {
            Order order = new Order()
                    .setTimeStamp(toTimeStamp(row[timestampIdx]))
                    .setBroker(row[brokerIdx])
                    .setId(Long.valueOf(row[orderIdx]))
                    .setType(row[typeIdx])
                    .setSymbol(row[symbolIdx])
                    .setQuantity(Integer.valueOf(row[quantityIdx]))
                    .setPrice(Double.valueOf(row[priceIdx]))
                    .setSide(SIDE.valueOf(row[sideIdx]));
            return order;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    // convert order to simple line with just broker and sequence id
    public static String toSimpleLine(Order order) {
        return order==null ? "" :
                (order.getBroker()==null ? "" : order.getBroker())
                + csvSplitBy
                + (order.getId()==0? "" : String.valueOf(order.getId()));
    }

    // convert order to protobuf message format
    public static OrderOuterClass.Order toProtoBuf(Order order) {
        try {
            OrderOuterClass.Order orderProto = OrderOuterClass.Order
                    .newBuilder()
                    .setTimestamp(order.timestamp)
                    .setBroker(order.broker)
                    .setId(order.id)
                    .setType(order.type)
                    .setSymbol(order.symbol)
                    .setQuantity(order.quantity)
                    .setPrice(order.price)
                    .setSide(OrderOuterClass.Order.sideType.valueOf(order.side.name())).build();
            return orderProto;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
