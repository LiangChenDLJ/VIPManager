package com.chen.zhou.liang.lancet.model;

import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.google.common.collect.ImmutableList;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.Optional;

public class CardsVisualTable {
    private final TableView<CardsRecord> tableView;

    public CardsVisualTable(TableView<CardsRecord> tableView) {
        this.tableView = tableView;
        ImmutableList<VisualColumn<CardsRecord, ?>> visualColumns = ImmutableList.of(
                new IntegerVisualColumn<>("ID", CardsRecord::getId),
                new StringVisualColumn<>("姓名", CardsRecord::getName),
                new StringVisualColumn<>("身份证", CardsRecord::getIdcard),
                new StringVisualColumn<>("手机号", CardsRecord::getPhone),
                new FloatVisualColumn<>("积分", CardsRecord::getCredit));
        tableView.setPlaceholder(new Label(""));
        tableView.getColumns().addAll(visualColumns.stream().map(VisualColumn::getTableColumn)
                .collect(ImmutableList.toImmutableList()));
    }

    public void updateTableView(ImmutableList<CardsRecord> cardsRecords){
        SortedList<CardsRecord> sortedList = new SortedList<>(FXCollections.observableList(cardsRecords));
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }

    public Optional<CardsRecord> getSelectedItem() {
        TableView.TableViewSelectionModel<CardsRecord> selectionModel = tableView.getSelectionModel();
        if (selectionModel.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(selectionModel.getSelectedItem());
    }
}
