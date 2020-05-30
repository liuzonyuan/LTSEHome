package com.ltse.processor;

import com.google.inject.Inject;
import com.ltse.dao.Order;
import com.ltse.filter.*;
import com.ltse.protobuf.OrderOuterClass;
import com.ltse.utils.ArgsParameterSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main class to process orders
 *
 * The output will be
 * accepted list
 * rejected list
 * accepted orders in proto buf format
 * rejected orders in proto buf format
 *
 * @author peterliu
 */
public class OrderProcessor implements AutoCloseable{
    private static final Logger LOGGER = LogManager.getLogger(OrderProcessor.class);
    private BufferedReader reader;

    @Inject
    List<Filter> filterList;

    public static final String inputFile = "/trades.csv";
    public static final String acceptedFile = "accepted.csv";
    public static final String rejectedFile = "rejected.csv";
    public static final String acceptedProtoBuf = "accepted.pb";
    public static final String rejectedProtoBuf = "rejected.pb";
    public static final String CSV = "csv";
    public static final String PROTO = "proto";

    /**
     * process the trade file
     */
    public void processOrder(ArgsParameterSettings argsParameterSettings) {
        try {
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(inputFile)));
            Map<Boolean, List<Order>> result = reader
                    .lines()
                    .map(Order::buildOrder)
                    .collect(Collectors.partitioningBy(s -> filter(s)));

            String outputPath = argsParameterSettings.getOutputFolder() == null ?
                    ClassLoader.getSystemResource(".").getPath() :
                    argsParameterSettings.getOutputFolder();

            generateOutput(result.get(true), outputPath + "/" + acceptedFile, CSV);
            generateOutput(result.get(false), outputPath + "/" + rejectedFile, CSV);
            generateOutput(result.get(true), outputPath + "/" + acceptedProtoBuf, PROTO);
            generateOutput(result.get(false), outputPath + "/" + rejectedProtoBuf, PROTO);
            LOGGER.info("Generate output files under: " + outputPath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * generate output file
     * @param list
     * @param outputFile
     * @param format
     * @throws IOException
     */
    private void generateOutput(List<Order> list, String outputFile, String format) throws IOException {
        if (format.equals(CSV)) {
            List<String> orderList = list.stream().map(Order::toSimpleLine).collect(Collectors.toList());
            FileWriter writer = new FileWriter(outputFile);
            for(String newLine : orderList) {
                writer.write(newLine);
                writer.write("\n"); // newline
            }
            writer.close();
        } else if (format.equals(PROTO)){
            List<OrderOuterClass.Order> orderList = list.stream().map(Order::toProtoBuf).collect(Collectors.toList());
            orderList.stream().filter(order -> order != null).forEach(order -> {
                try {
                    order.writeTo(new FileOutputStream(outputFile));
                } catch (IOException e) {
                    LOGGER.error("Error when write order to proto buffer: " + order.getId());
                }
            });
        }
    }

    /**
     * go through each filter
     * @param order
     * @return
     */
    private boolean filter(Order order) {
        for(Filter filter : filterList) {
            if (!filter.applyFilter(order)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }
}
