package com.chen.zhou.liang.lancet.controller;

import com.chen.zhou.liang.lancet.StageManager;
import com.chen.zhou.liang.lancet.model.CardsUpdatePane;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.chen.zhou.liang.lancet.utils.DisplayableException;
import com.chen.zhou.liang.lancet.utils.MessageDisplayer;
import com.chen.zhou.liang.lancet.utils.TimeUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.jooq.DSLContext;

import java.util.Optional;
import java.io.IOException;

import static com.chen.zhou.liang.lancet.storage.orm.Tables.CARDS;
import static com.chen.zhou.liang.lancet.storage.orm.Tables.TRANSHISTORY;

public class CardsUpdateController {
    @FXML
    Button updateButton;

    @FXML
    GridPane attributeListPane;

    @FXML
    TextField messageTextField;

    private final DSLContext dslContext;
    private final StageManager stageManager;
    private final MessageDisplayer messageDisplayer;
    CardsUpdatePane cardsUpdatePane;

    @Inject
    public CardsUpdateController(DSLContext dslContext, StageManager stageManager) {
        this.dslContext = dslContext;
        this.stageManager = stageManager;
        this.messageDisplayer = new MessageDisplayer();
    }

    @FXML
    public void initialize(CardsRecord cardsRecord){
        messageDisplayer.initialize(messageTextField);
        this.cardsUpdatePane = new CardsUpdatePane(attributeListPane, cardsRecord);
    };

    void validateIdcard(CardsRecord cardsRecord) throws DisplayableException {
        String idcard = cardsRecord.getIdcard();
        // 身份证可为空
        if (idcard == null || idcard.isEmpty()) {
            return;
        }
        if (dslContext.selectCount().from(CARDS)
                .where(CARDS.IDCARD.eq(idcard).and(CARDS.ID.notEqual(cardsRecord.getId())))
                .fetchOne(0, int.class) > 0) {
            throw new DisplayableException("身份证 "  + idcard + " 已存在");
        }
    }

    void validatePhone(CardsRecord cardsRecord) throws DisplayableException  {
        String phone = cardsRecord.getPhone();
        // 手机号可为空
        if (phone == null || phone.isEmpty()) {
            return;
        }
        if (dslContext.selectCount().from(CARDS)
                .where(CARDS.PHONE.eq(phone).and(CARDS.ID.notEqual(cardsRecord.getId())))
                .fetchOne(0, int.class) > 0) {
            throw new DisplayableException("手机号码 "  + phone + " 已存在");
        }
    }

    @FXML
    void updateButtonHandler() {
        try {
            CardsRecord cardsRecord = new CardsRecord();
            cardsUpdatePane.fillCardsRecord(cardsRecord);
            validateIdcard(cardsRecord);
            validatePhone(cardsRecord);

            int numberOfRowsUpdated = dslContext
                    .update(CARDS)
                    .set(CARDS.NAME, cardsRecord.getName())
                    .set(CARDS.IDCARD, cardsRecord.getIdcard())
                    .set(CARDS.PHONE, cardsRecord.getPhone())
                    .where(CARDS.ID.eq(cardsRecord.getId()))
                    .execute();
            assert(numberOfRowsUpdated == 1);
            stageManager.closeCardsUpdateStage();
        } catch (DisplayableException e) {
            messageDisplayer.displayMessage("会员卡信息更新失败. " + e.getMessage());
            return;
        } catch (IOException e) {
            messageDisplayer.displayMessage("[内部错误] 会员卡信息更新失败. ", e);
            return;
        }
    }
}
