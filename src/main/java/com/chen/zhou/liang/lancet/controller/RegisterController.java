package com.chen.zhou.liang.lancet.controller;

import com.chen.zhou.liang.lancet.StageManager;
import com.chen.zhou.liang.lancet.model.CardsRegisterPane;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.chen.zhou.liang.lancet.utils.DisplayableException;
import com.chen.zhou.liang.lancet.utils.MessageDisplayer;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
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
    void regButtonHandler(){
        CardsRecord cardsRecord = dslContext.newRecord(CARDS);
        try {
            cardsRegisterPane.fillCardsRecord(cardsRecord);
        } catch (DisplayableException e) {
            messageDisplayer.displayMessage(e.getMessage());
            return;
        }
        cardsRecord.store();
        stageManager.closeRegisterStage();
    }
}