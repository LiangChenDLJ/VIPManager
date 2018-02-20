package com.watsonLiang;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController extends MsgControllerPrototype {
    @FXML
    Button regButton;

    @FXML
    ChoiceBox<String> searchChoiceBox;

    @FXML
    Button searchButton;

    @FXML
    Button changepassButton;

    @FXML
    TableView<ItemRecord> searchTable;

    @FXML
    TextField searchInput;

    @FXML
    Button creditButton;

    @FXML
    TextField creditInput;

    @FXML
    Button historyButton;

    @FXML
    public void initialize(){
        searchChoiceBox.setValue(DataModel.dataAttrDisplay[0]);
        searchChoiceBox.setItems(FXCollections.observableArrayList(DataModel.searchAttrDisplay));

        SortedList sortedList = new SortedList(FXCollections.observableArrayList());
        for(int i = 0; i< DataModel.dataAttrDisplay.length; i++){
            TableColumn<ItemRecord, String> newColumn = new TableColumn<>(DataModel.dataAttrDisplay[i]);
            newColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemRecord, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemRecord, String> p) {
                    return new ReadOnlyObjectWrapper<>(p.getValue().attrs.get(p.getTableColumn().getText()));
                }});
            searchTable.getColumns().add(newColumn);
        }

    }

    private void updateTableView(String[][] data){
        ObservableList<ItemRecord> ol = FXCollections.observableArrayList();
        for(String[] attrs : data){
            ol.addAll(new ItemRecord(DataModel.dataAttrDisplay, attrs));
        }
        SortedList<ItemRecord> sl = new SortedList<>(ol);
        sl.comparatorProperty().bind(searchTable.comparatorProperty());
        searchTable.setItems(sl);
    }

    @FXML
    void inputEnterHandler(KeyEvent e){
        int i;
        switch(e.getCode()){
            case ENTER:
                doSearch(searchChoiceBox.getValue(), searchInput.getText());
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

    void doSearch(String attr, String val){
        updateTableView(Main.dbconn.query(DataModel.dataAttr[DataModel.displayIndex(attr)], val));
    }

    @FXML
    void searchButtonHandler(){
        doSearch(searchChoiceBox.getValue(), searchInput.getText());
    }

    @FXML
    void regButtonHandler(){
        Stage regStage = new Stage();
        try {
            Parent regroot = FXMLLoader.load(getClass().getResource("/view/reg.fxml"));
            regStage.setTitle("注册");
            regStage.setScene(new Scene(regroot));
            regStage.initModality(Modality.APPLICATION_MODAL);
            regStage.show();
        }catch(Exception exception){
            exception.printStackTrace();
        }

    }

    @FXML
    void creditButtonHandler(){
        String id = searchTable.getSelectionModel().getSelectedItem().attrs.get("ID");
        Main.dbconn.update(id, true, creditInput.getText());
        creditInput.setText("");
    }

    @FXML
    void changepassButtonHandler(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/changepass.fxml"));
            Stage changepassStage = new Stage();
            changepassStage.setTitle("修改密码");
            changepassStage.setScene(new Scene(root));
            changepassStage.initModality(Modality.APPLICATION_MODAL);
            changepassStage.show();
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }

    @FXML
    void historyButtonHandler(){
        TableView.TableViewSelectionModel<ItemRecord> selectionmodel = searchTable.getSelectionModel();
        if(selectionmodel.isEmpty()){
            displayMessage("错误：未选择会员卡");
            return;
        }
        ItemRecord cr = selectionmodel.getSelectedItem();
        String id = cr.attrs.get("ID");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/history.fxml"));
            HistoryController controller = new HistoryController(id);
            loader.setController(controller);
            Parent root = loader.load();
            Stage historyStage = new Stage();
            historyStage.setTitle("积分历史");
            historyStage.setScene(new Scene(root));
            historyStage.initModality(Modality.APPLICATION_MODAL);
            historyStage.show();
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
}
