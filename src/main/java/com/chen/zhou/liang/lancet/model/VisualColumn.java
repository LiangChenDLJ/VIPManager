package com.chen.zhou.liang.lancet.model;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.function.Function;

public abstract class VisualColumn<S, T> {
    private final String columnDisplayName;
    Function<S, T> valueFactory;

    public VisualColumn(String columnDisplayName, Function<S, T> valueFactory) {
        this.columnDisplayName = columnDisplayName;
        this.valueFactory = valueFactory;
    }

    public TableColumn<S, String> getTableColumn() {
        TableColumn<S, String> tableColumn = new TableColumn<>(columnDisplayName);
        tableColumn.setComparator(Comparator.nullsFirst(getComparator()));
        tableColumn.setCellValueFactory(observableValue ->
                new ReadOnlyObjectWrapper<>(convertNullable(valueFactory.apply(observableValue.getValue()))));
        return tableColumn;
    }

    private String convertNullable(@Nullable T rawValue) {
        return rawValue == null ? "" : convert(rawValue);
    }

    abstract protected String convert(T rawValue);

    abstract protected Comparator<String> getComparator();
}