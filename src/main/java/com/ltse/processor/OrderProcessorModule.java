package com.ltse.processor;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.ltse.filter.*;

import java.util.List;

/**
 * module for OrderProcessor
 *
 * @author peterliu
 */
public class OrderProcessorModule extends AbstractModule {
    @Override
    protected void configure() {}

    @Provides
    public List<Filter> provideFilters(
            FormatFilter formatFilter,
            SymbolFilter symbolFilter,
            WindowFilter windowFilter,
            UniqueFilter uniqueFilter) {
        return ImmutableList.of(
                formatFilter,
                symbolFilter,
                windowFilter,
                uniqueFilter);
    }
}
