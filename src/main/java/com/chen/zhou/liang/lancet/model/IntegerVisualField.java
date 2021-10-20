package com.chen.zhou.liang.lancet.model;

import java.util.function.BiConsumer;

public class IntegerVisualField<S> extends VisualField<S, Integer>{
    public IntegerVisualField(String displayName, BiConsumer<S, Integer> updaterFunction, boolean isRequired) {
        super(displayName, updaterFunction, isRequired);
    }

    @Override
    public Integer convert(String inputString) {
        return Integer.parseInt(inputString);
    }

    @Override
    public boolean checkStringFormat(String stringValue) {
        return stringValue.matches("[0-9]+");
    }
}
