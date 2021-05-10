package com.chen.zhou.liang.lancet.model;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.function.Function;

public class TimeVisualColumn<S> extends VisualColumn<S, String> {
    public TimeVisualColumn(String columnDisplayName, Function<S, String> valueFactory) {
        super(columnDisplayName, valueFactory);
    }

    @Override
    public String convert(String rawValue) {
        // TODO: format time string
        return rawValue;
    }

    @Override
    public Comparator<String> getComparator() {
        return Comparator.comparing(Function.identity());
    }
}
