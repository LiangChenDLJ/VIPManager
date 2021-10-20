package com.chen.zhou.liang.lancet.model;

import com.google.common.collect.ImmutableList;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.Optional;

public abstract class VisualTable<S> {
    protected final TableView<S> tableView;

    public VisualTable(TableView<S> tableView, ImmutableList<VisualColumn<S, ?>> visualColumns) {
        this.tableView = tableView;
        tableView.setPlaceholder(new Label(""));
        tableView.getColumns().addAll(visualColumns.stream().map(VisualColumn::getTableColumn)
                .collect(ImmutableList.toImmutableList()));
    }

    public void updateTableView(ImmutableList<S> items) {
        SortedList<S> sortedList = new SortedList<S>(FXCollections.observableList(items));
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }

    public Optional<S> getSelectedItem() {
        TableView.TableViewSelectionModel<S> selectionModel = tableView.getSelectionModel();
        if (selectionModel.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(selectionModel.getSelectedItem());
    }
}
