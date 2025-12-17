package com.chen.zhou.liang.lancet.controller;

import static com.chen.zhou.liang.lancet.storage.orm.Tables.CARDS;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.chen.zhou.liang.lancet.model.HistoryCardInfoVisualTable;
import com.chen.zhou.liang.lancet.model.HistoryVisualTable;
import com.chen.zhou.liang.lancet.model.pojo.TranshistoryWithCardInfo;
import com.chen.zhou.liang.lancet.storage.DatabaseClient;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.chen.zhou.liang.lancet.utils.MessageDisplayer;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jooq.DSLContext;

import javax.annotation.Nullable;


public class HistoryController {
    @FXML
    TableView<CardsRecord> cardInfoTableView;

    @FXML
    TableView<TranshistoryWithCardInfo> historyTableView;

    @FXML
    TextField messageTextField;

    @FXML
    TextField searchInput;

    @FXML
    Button searchButton;

    @FXML
    DatePicker queryDateRangeStart;

    @FXML
    DatePicker queryDateRangeEnd;

    private final MessageDisplayer messageDisplayer;
    private final DSLContext dslContext;
    private final DatabaseClient databaseClient;
    private HistoryCardInfoVisualTable cardInfoVisualTable;
    private HistoryVisualTable historyVisualTable;

    @Inject
    public HistoryController(DSLContext dslContext, DatabaseClient databaseClient) {
        this.dslContext = dslContext;
        this.databaseClient = databaseClient;
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
                messageDisplayer.displayMessage("错误：会员卡ID " + id + " 不存在");
                return;
            }
            cardInfoVisualTable.updateTableView(ImmutableList.of(card));
            searchInput.setText(Integer.toString(id));
            searchButtonHandler();
        } catch (Exception e) {
            messageDisplayer.displayMessage("读取会员积分历史失败.", e);
        }
    }

    @FXML
    void searchButtonHandler(){
        @Nullable LocalDate dateStart = queryDateRangeStart.getValue();
        Optional<LocalDateTime> optionalDateTimeStart;
        if (dateStart == null) {
            optionalDateTimeStart = Optional.empty();
        } else {
            optionalDateTimeStart = Optional.of(dateStart.atStartOfDay());
        }
        @Nullable LocalDate dateEnd = queryDateRangeEnd.getValue();
        Optional<LocalDateTime> optionalDateTimeEnd;
        if (dateEnd == null) {
            optionalDateTimeEnd = Optional.empty();
        } else {
            optionalDateTimeEnd = Optional.of(dateEnd.plusDays(1).atStartOfDay());
        }
        String searchInputText = searchInput.getText();
        int cardId;
        try {
            cardId = Integer.parseInt(searchInputText);
        } catch (NumberFormatException ignored) {
            messageDisplayer.displayMessage("积分历史界面仅支持按照会员卡ID搜索。错误搜索条件：" + searchInputText);
            return;
        }
        historyVisualTable.updateTableView(
                databaseClient.QueryTranshistoryAndCardsRecord(
                        cardId, optionalDateTimeStart, optionalDateTimeEnd)
        );
    }

    @FXML
    void startDateStartHandler(){
        queryDateRangeEnd.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(queryDateRangeStart.getValue()));
                    }});
    }

    @FXML
    void endDateStartHandler() {
        queryDateRangeStart.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(queryDateRangeEnd.getValue()));
                    }});
    }
}
