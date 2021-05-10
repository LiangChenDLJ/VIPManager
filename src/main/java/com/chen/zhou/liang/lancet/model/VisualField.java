package com.chen.zhou.liang.lancet.model;

import com.chen.zhou.liang.lancet.utils.DisplayableException;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class VisualField<S, T> {
    private final String displayName;
    private final BiConsumer<S, T> updaterFunction;
    private final TextField textField;
    private final boolean isRequired;

    public VisualField(String displayName, BiConsumer<S, T> updaterFunction, boolean isRequired) {
        this.displayName = displayName;
        this.updaterFunction = updaterFunction;
        this.textField = new TextField();
        this.isRequired = isRequired;
    }

    public void addRowToPane(GridPane gridPane, int index) {
        gridPane.addRow(index, new Text(displayName), textField);
    }

    public void fillValue(S s) throws DisplayableException {
        String textValue = textField.getText();
        if (textValue.isEmpty()) {
            if (isRequired) throw new DisplayableException("错误：" + displayName + " 不能为空");
            else return;
        }
        if (!checkStringFormat(textValue)) {
            throw new DisplayableException("格式错误：" + displayName + " 不应为" + textField.getText());
        }
        updaterFunction.accept(s, convert(textField.getText()));
    }

    abstract T convert(String inputString);

    abstract boolean checkStringFormat(String stringValue);
}
