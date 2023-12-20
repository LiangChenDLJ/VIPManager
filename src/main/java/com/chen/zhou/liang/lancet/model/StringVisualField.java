package com.chen.zhou.liang.lancet.model;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class StringVisualField<S> extends VisualField<S, String>{
    public StringVisualField(String displayName, BiConsumer<S, String> updaterFunction, boolean isRequired) {
        super(displayName, updaterFunction, isRequired);
    }

    @Override
    public String convert(@Nullable String inputString) {
        return inputString == null ? "" : inputString;
    }

    public void setValue(String s) {
        getTextField().setText(s);
    }

    @Override
    public boolean checkStringFormat(String stringValue) {
        return !stringValue.isEmpty();
    }
}
