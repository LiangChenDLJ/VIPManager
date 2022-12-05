package com.chen.zhou.liang.lancet.model;

import java.util.Comparator;
import java.util.function.Function;

public class StringVisualColumn<S> extends VisualColumn<S, String> {
    public StringVisualColumn(String displayName, Function<S, String> valueFactory) {
        super(displayName, valueFactory);
    }

    @Override
    public String convert(String rawValue) {
        return rawValue;
    }

    @Override
    public Comparator<String> getComparator() {
        return Comparator.comparing(Function.identity());
    }
}
