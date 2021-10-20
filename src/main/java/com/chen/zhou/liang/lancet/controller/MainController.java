package com.chen.zhou.liang.lancet.controller;

import com.chen.zhou.liang.lancet.StageManager;
import com.chen.zhou.liang.lancet.model.CardsVisualTable;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.chen.zhou.liang.lancet.utils.MessageDisplayer;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;

import static com.chen.zhou.liang.lancet.storage.orm.Tables.CARDS;

import java.io.IOException;
import java.util.Optional;

public class MainController {
    @FXML
    Button regButton;

    @FXML
    Button searchButton;

    @FXML
    Button changePasswordButton;

    @FXML
    TableView<CardsRecord> cardsTableView;

    @FXML
    TextField searchInput;

    @FXML
    Button creditButton;

    @FXML
    TextField creditInput;

    @FXML
    Button historyButton;

    @FXML
    Button deleteButton;

    @FXML
    TextField messageTextField;

    private final DSLContext dslContext;
    private final StageManager stageManager;
    private final MessageDisplayer messageDisplayer;
    private CardsVisualTable cardsVisualTable;

    @Inject
    public MainController(DSLContext dslContext, StageManager stageManager) {
        this.dslContext = dslContext;
        this.stageManager = stageManager;
        this.messageDisplayer = new MessageDisplayer();
    }

    @FXML
    public void initialize(){
        messageDisplayer.initialize(messageTextField);
        cardsVisualTable = new CardsVisualTable(cardsTableView);
    }

    @FXML
    void inputEnterHandler(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER) {
            searchCards();
        }
    }

    void searchCards(){
        String searchValue = searchInput.getText();
        final String likePhrase = "%" + searchValue + "%";
        Condition condition = CARDS.NAME.like(likePhrase).or(CARDS.IDCARD.like(likePhrase)).or(CARDS.PHONE.like(likePhrase));
        try {
            int intVal = Integer.parseInt(searchValue);
            condition.or(CARDS.ID.eq(intVal));
        } catch (NumberFormatException ignored) {
        }
        Result<CardsRecord> cards =  dslContext.selectFrom(CARDS).where(condition).fetch();
        cardsVisualTable.updateTableView(ImmutableList.copyOf(cards));
    }

    @FXML
    void searchButtonHandler(){
        searchCards();
    }

    @FXML
    void creditEnterHandler(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER){
            updateCredit();
        }
    }

    @FXML
    void regButtonHandler(){
        try {
            stageManager.showRegisterStage();
        } catch (IOException e) {
            messageDisplayer.displayMessage("[内部错误]启动注册界面失败", e);
            e.printStackTrace();
        }
    }

    void updateCredit() {
        String creditTextInput = creditInput.getText();
        if (creditTextInput.length() == 0){
            messageDisplayer.displayMessage("请输入积分");
            return;
        }
        float creditAmountToUpdate;
        try {
            creditAmountToUpdate = Float.parseFloat(creditTextInput);
        } catch (NumberFormatException e) {
            messageDisplayer.displayMessage("积分格式错误，不应为： " + creditTextInput, e);
            return;
        }

        Optional<CardsRecord> cardsRecordOptional = cardsVisualTable.getSelectedItem();
        if (!cardsRecordOptional.isPresent()) {
           messageDisplayer.displayMessage("错误：未选择会员卡");
           return;
        }
        CardsRecord cardsRecord = cardsRecordOptional.get();

        int numberOfRowsUpdated = dslContext
                .update(CARDS)
                .set(CARDS.CREDIT, CARDS.CREDIT.plus(creditAmountToUpdate))
                .where(CARDS.ID.eq(cardsRecord.getId()))
                .execute();
        assert(numberOfRowsUpdated == 1);
        searchCards();
        messageDisplayer.displayMessage("积分成功");
    }

    @FXML
    void creditButtonHandler(){
        updateCredit();
    }

    @FXML
    void changePasswordButtonHandler() {
        try {
            stageManager.showChangePasswordStage();
        } catch(IOException e){
            messageDisplayer.displayMessage("[内部错误] 启动注册界面失败", e);
        }
    }

    @FXML
    void historyButtonHandler(){
        Optional<CardsRecord> selectedCard = cardsVisualTable.getSelectedItem();
        if (selectedCard.isEmpty()) {
            messageDisplayer.displayMessage("错误：未选择会员卡");
            return;
        }
        int cardId = selectedCard.get().getId();
        try {
            stageManager.showHistoryStage(cardId);
        } catch(IOException e){
            messageDisplayer.displayMessage("[内部错误] 启动积分历史界面失败", e);
        }
    }

    @FXML
    void deleteButtonHandler(){
        Optional<CardsRecord> cardsRecordOptional = cardsVisualTable.getSelectedItem();
        if (cardsRecordOptional.isEmpty()) {
            messageDisplayer.displayMessage("错误：未选择会员卡");
            return;
        }
        CardsRecord selectedCard = cardsRecordOptional.get();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认删除");
        alert.setContentText("确认删除会员卡吗？删除后无法恢复");
        Optional<ButtonType> confirmation = alert.showAndWait();
        if (confirmation.isPresent() && (confirmation.get() == ButtonType.OK)) {
            int rowsUpdated = dslContext.deleteFrom(CARDS).where(CARDS.ID.eq(selectedCard.getId())).execute();
            assert(rowsUpdated == 1);
            messageDisplayer.displayMessage("删除成功");
            searchCards();
        }
    }
}
