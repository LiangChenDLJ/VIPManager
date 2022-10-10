package com.chen.zhou.liang.lancet.model;

import java.util.Comparator;
import java.util.function.Function;

public class FloatVisualColumn<S> extends VisualColumn<S, Float> {
    public FloatVisualColumn(String displayName, Function<S, Float> valueFactory) {
        super(displayName, valueFactory);
    }

    @Override
    public String convert(Float rawValue) {
        return  String.format("%.2f", rawValue);
    }

    @Override
    public Comparator<String> getComparator() {
        return Comparator.comparingDouble(Float::valueOf);
    }
}
