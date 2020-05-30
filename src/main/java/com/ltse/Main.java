package com.ltse;

import com.beust.jcommander.JCommander;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ltse.filter.WindowFilter;
import com.ltse.processor.OrderProcessor;
import com.ltse.processor.OrderProcessorModule;
import com.ltse.utils.ArgsParameterSettings;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class to run the application
 *
 * @author peterliu
 */
public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        ArgsParameterSettings argsParameterSettings = new ArgsParameterSettings();
        JCommander.newBuilder()
                .addObject(argsParameterSettings)
                .build()
                .parse(args);

        Injector injector = Guice.createInjector(new OrderProcessorModule());
        try (OrderProcessor processManager = injector.getInstance(OrderProcessor.class)) {
            WindowFilter.mode = argsParameterSettings.getWindowMode();
            processManager.processOrder(argsParameterSettings);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "Errors when running main: " + e.getMessage());
            System.exit(1);
        }
    }
}
