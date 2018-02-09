package com.watsonLiang;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.util.Callback;

import java.util.HashMap;

class CardRecord {
    HashMap<String, String> attrs;
    public CardRecord(String[] dataAttrs, String[] dataVals){
        attrs = new HashMap<>();
        for(int i = 0; i < dataAttrs.length; i++){
            attrs.put(dataAttrs[i], dataVals[i]);
        }
    }
};



public class MainController {
    @FXML
    ChoiceBox<String> searchChoiceBox;

    @FXML
    Button searchButton;

    @FXML
    TableView<CardRecord> searchTable;

    @FXML
    TextField searchInput;

    @FXML
    public void initialize(){
        searchChoiceBox.setValue(DataModel.dataAttrDisplay[0]);
        searchChoiceBox.setItems(FXCollections.observableArrayList(DataModel.searchAttrDisplay));

        SortedList sortedList = new SortedList(FXCollections.observableArrayList());
        for(int i = 0; i< DataModel.dataAttrDisplay.length; i++){
            TableColumn<CardRecord, String> newColumn = new TableColumn<>(DataModel.dataAttrDisplay[i]);
            newColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CardRecord, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<CardRecord, String> p) {
                    return new ReadOnlyObjectWrapper<>(p.getValue().attrs.get(p.getTableColumn().getText()));
                }});
            searchTable.getColumns().add(newColumn);
        }

    }

    private void updateTableView(String[][] data){
        ObservableList<CardRecord> ol = FXCollections.observableArrayList();
        for(String[] attrs : data){
            ol.addAll(new CardRecord(DataModel.dataAttrDisplay, attrs));
        }
        SortedList<CardRecord> sl = new SortedList<>(ol);
        sl.comparatorProperty().bind(searchTable.comparatorProperty());
        searchTable.setItems(sl);
    }

    @FXML
    void inputEnterHandler(KeyEvent e){
        int i;
        switch(e.getCode()){
            case ENTER:
                doSearch();
                break;
            case UP:
                i = DataModel.displayIndex(searchChoiceBox.getValue());
                i = (i + DataModel.searchAttrDisplay.length - 1) % DataModel.searchAttrDisplay.length;
                searchChoiceBox.setValue(DataModel.searchAttrDisplay[i]);
                break;
            case DOWN:
                i = DataModel.displayIndex(searchChoiceBox.getValue());
                i = (i+1) % DataModel.searchAttrDisplay.length;
                searchChoiceBox.setValue(DataModel.searchAttrDisplay[i]);
                break;
        }
    }

    void doSearch(){
        updateTableView(Main.dbconn.query(DataModel.dataAttr[DataModel.displayIndex(searchChoiceBox.getValue())], searchInput.getText()));
    }

    @FXML
    void searchButtonHandler(MouseEvent e){
        doSearch();
    }
}
