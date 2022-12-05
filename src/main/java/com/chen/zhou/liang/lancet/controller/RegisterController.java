package com.chen.zhou.liang.lancet.controller;

import com.chen.zhou.liang.lancet.StageManager;
import com.chen.zhou.liang.lancet.model.CardsRegisterPane;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.chen.zhou.liang.lancet.utils.DisplayableException;
import com.chen.zhou.liang.lancet.utils.MessageDisplayer;
import com.chen.zhou.liang.lancet.utils.TimeUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.jooq.DSLContext;

import static com.chen.zhou.liang.lancet.storage.orm.Tables.CARDS;

public class RegisterController {
    @FXML
    Button regButton;

    @FXML
    GridPane attributeListPane;

    @FXML
    TextField messageTextField;

    private final DSLContext dslContext;
    private final StageManager stageManager;
    private final MessageDisplayer messageDisplayer;
    CardsRegisterPane cardsRegisterPane;

    @Inject
    public RegisterController(DSLContext dslContext, StageManager stageManager) {
        this.dslContext = dslContext;
        this.stageManager = stageManager;
        this.messageDisplayer = new MessageDisplayer();
    }

    @FXML
    public void initialize(){
        messageDisplayer.initialize(messageTextField);
        this.cardsRegisterPane = new CardsRegisterPane(attributeListPane);
    };

    @FXML
    void regButtonHandler() {
        CardsRecord cardsRecord = dslContext.newRecord(CARDS);
        try {
            cardsRegisterPane.fillCardsRecord(cardsRecord);
            int cardsWithSameIdCount = dslContext.selectCount().from(CARDS).where(CARDS.ID.eq(cardsRecord.getId()))
                    .fetchOne(0, int.class);
            if (cardsWithSameIdCount > 0) {
                messageDisplayer.displayMessage("新建失败，ID "  + cardsRecord.getId() + " 已存在");
                return;
            }
        } catch (DisplayableException e) {
            messageDisplayer.displayMessage(e.getMessage());
            return;
        }
        cardsRecord.setRegtime(TimeUtils.getCurrentTimeAsText());
        try {
            cardsRecord.store();
        }catch (RuntimeException re) {
            re.printStackTrace();
            messageDisplayer.displayMessage("[内部错误] 会员卡新建失败");
        }
        stageManager.closeRegisterStage();
    }

    private void alert () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认删除");
        alert.setContentText("确认删除会员卡吗？删除后无法恢复");
        alert.showAndWait();
    }
}