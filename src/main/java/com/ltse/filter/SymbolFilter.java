package com.ltse.filter;

import com.ltse.dao.Order;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SYMBOL FILTER
 * Only orders for symbols actually traded on the exchange should be accepted
 *
 * @author peterliu
 */
public class SymbolFilter implements Filter{
    private static final String SYMBOLS_FILE = "/symbols.txt";

    @Override
    public boolean applyFilter(Order order) {
        Set<String> set = loadSymbolList();
        return (set.contains(order.getSymbol()));
    }

    private Set<String> loadSymbolList() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(SYMBOLS_FILE)));
//            Path path = Paths.get(ClassLoader.getSystemResource(SYMBOLS_FILE).toURI());
//            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);) {
                return reader.lines().collect(Collectors.toSet());
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }
}
