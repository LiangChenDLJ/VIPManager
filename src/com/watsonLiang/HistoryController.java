package com.watsonLiang;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.Comparator;

public class HistoryController extends MsgControllerPrototype {
    @FXML
    TableView headTable;

    @FXML
    TableView historyTable;

    public HistoryController(String id){
        this.id = id;
    }

    String id;

    @FXML
    public void initialize(){
        headTable.setPlaceholder(new Label(""));
        for(int i = 0; i< DataModel.historyHeadAttrDisplay.length; i++){
            TableColumn<ItemRecord, String> newColumn = new TableColumn<>(DataModel.historyHeadAttrDisplay[i]);
            switch(DataModel.dataType[i]){
                case real:
                    newColumn.setComparator(Comparator.comparingDouble(s->Double.valueOf(s)));
                    break;
                case integer:
                    newColumn.setComparator(Comparator.comparingInt(s->Integer.valueOf(s)));
                    break;
            }
            newColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemRecord, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemRecord, String> p) {
                    return new ReadOnlyObjectWrapper<>(p.getValue().attrs.get(p.getTableColumn().getText()));
                }});
            headTable.getColumns().add(newColumn);
        }

        historyTable.setPlaceholder(new Label(""));
        for(int i = 0; i< DataModel.historyAttrDisplay.length; i++){
            TableColumn<ItemRecord, String> newColumn = new TableColumn<>(DataModel.historyAttrDisplay[i]);
            switch(DataModel.dataType[i]){
                case real:
                    newColumn.setComparator(Comparator.comparingDouble(s->Double.valueOf(s)));
                    break;
                case integer:
                    newColumn.setComparator(Comparator.comparingInt(s->Integer.valueOf(s)));
                    break;
            }
            newColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemRecord, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemRecord, String> p) {
                    return new ReadOnlyObjectWrapper<>(p.getValue().attrs.get(p.getTableColumn().getText()));
                }});
            historyTable.getColumns().add(newColumn);
        }
        try {
            String[][] headdata = Main.dbconn.query("ID", id);
            if(headdata.length == 0) throw new Exception("Error: ID " + id + " doesn't exist");
            ObservableList<ItemRecord> ol = FXCollections.observableArrayList();
            for(String[] attrs : headdata){
                ol.addAll(new ItemRecord(DataModel.historyHeadAttrDisplay, attrs));
            }
            SortedList<ItemRecord> sl = new SortedList<>(ol);
            sl.comparatorProperty().bind(headTable.comparatorProperty());
            headTable.setItems(sl);

            String[][] historydata = Main.dbconn.queryHistory(id);
            ol = FXCollections.observableArrayList();
            for(String[] attrs : historydata){
                ol.addAll(new ItemRecord(DataModel.historyAttrDisplay, attrs));
            }
            sl = new SortedList<>(ol);
            sl.comparatorProperty().bind(historyTable.comparatorProperty());
            historyTable.setItems(sl);
        }catch(Exception e){
            System.out.println(e);
        }
    }
}