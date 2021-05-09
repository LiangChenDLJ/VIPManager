package com.chen.zhou.liang.lancet.model;

import java.util.Comparator;
import java.util.function.Function;

public class FloatVisualColumn<S> extends VisualColumn<S, Float> {
    public FloatVisualColumn(String columnDisplayName, Function<S, Float> valueFactory) {
        super(columnDisplayName, valueFactory);
    }

    @Override
    public String convert(Float rawValue) {
        return rawValue.toString();
    }

    @Override
    public Comparator<String> getComparator() {
        return Comparator.comparingDouble(Float::valueOf);
    }
}
