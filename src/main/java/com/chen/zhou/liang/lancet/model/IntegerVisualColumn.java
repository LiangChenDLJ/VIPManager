package com.chen.zhou.liang.lancet.model;

import java.util.Comparator;
import java.util.function.Function;

public class IntegerVisualColumn<S> extends VisualColumn<S, Integer> {
    public IntegerVisualColumn(String displayName, Function<S, Integer> valueFactory) {
        super(displayName, valueFactory);
    }

    @Override
    public String convert(Integer rawValue) {
        return rawValue.toString();
    }

    @Override
    public Comparator<String> getComparator() {
        return Comparator.comparingDouble(Double::valueOf);
    }
}
