package com.chen.zhou.liang.lancet.model;

import com.chen.zhou.liang.lancet.utils.DisplayableException;

import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class StringVisualField<S> extends VisualField<S, String>{
    public StringVisualField(String displayName, BiConsumer<S, String> updaterFunction, boolean isRequired) {
        super(displayName, updaterFunction, isRequired);
    }

    @Override
    public String convert(String inputString) {
        return inputString;
    }

    @Override
    public boolean checkStringFormat(String stringValue) {
        return !stringValue.isEmpty();
    }
}
