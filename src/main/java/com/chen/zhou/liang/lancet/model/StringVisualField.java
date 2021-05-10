package com.chen.zhou.liang.lancet.model;

import com.chen.zhou.liang.lancet.utils.DisplayableException;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class StringVisualField<S> extends VisualField<S, String>{
    public StringVisualField(String displayName, BiConsumer<S, String> updaterFunction, boolean isRequired) {
        super(displayName, updaterFunction, isRequired);
    }

    @Override
    public String convert(@Nullable String inputString) {
        return inputString == null ? "" : inputString;
    }

    @Override
    public boolean checkStringFormat(String stringValue) {
        return !stringValue.isEmpty();
    }
}
