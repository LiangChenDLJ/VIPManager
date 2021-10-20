package com.chen.zhou.liang.lancet.controller;

import static com.chen.zhou.liang.lancet.storage.orm.Tables.CARDS;
import static com.chen.zhou.liang.lancet.storage.orm.Tables.TRANSHISTORY;

import com.chen.zhou.liang.lancet.model.HistoryCardInfoVisualTable;
import com.chen.zhou.liang.lancet.model.HistoryVisualTable;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.TranshistoryRecord;
import com.chen.zhou.liang.lancet.utils.MessageDisplayer;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.jooq.DSLContext;

import org.jooq.Result;

public class HistoryController {
    @FXML
    TableView<CardsRecord> cardInfoTableView;

    @FXML
    TableView<TranshistoryRecord> historyTableView;

    @FXML
    TextField messageTextField;

    private final DSLContext dslContext;
    private final MessageDisplayer messageDisplayer;
    private HistoryCardInfoVisualTable cardInfoVisualTable;
    private HistoryVisualTable historyVisualTable;

    @Inject
    public HistoryController(DSLContext dslContext) {
        this.dslContext = dslContext;
        this.messageDisplayer = new MessageDisplayer();
    }

    @FXML
    public void initialize(int id) {
        messageDisplayer.initialize(messageTextField);
        cardInfoVisualTable = new HistoryCardInfoVisualTable(cardInfoTableView);
        historyVisualTable = new HistoryVisualTable(historyTableView);

        try {
            CardsRecord card = dslContext.selectFrom(CARDS).where(CARDS.ID.eq(id)).fetchAny();
            if (card == null) {
                messageDisplayer.displayMessage("错误：会员卡ID" + id + "不存在");
                return;
            }
            cardInfoVisualTable.updateTableView(ImmutableList.of(card));
            Result<TranshistoryRecord> historyTransactions = dslContext.selectFrom(TRANSHISTORY).where(TRANSHISTORY.ID.eq(id)).fetch();
            historyVisualTable.updateTableView(ImmutableList.copyOf(historyTransactions));
        } catch (Exception e) {
            messageDisplayer.displayMessage("读取会员积分历史失败.", e);
            return;
        }
    }
}