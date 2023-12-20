package com.chen.zhou.liang.lancet.model;

import com.chen.zhou.liang.lancet.model.pojo.TranshistoryWithCardInfo;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.TranshistoryRecord;
import com.google.common.collect.ImmutableList;
import javafx.scene.control.TableView;

import java.util.function.Function;

public class HistoryVisualTable extends VisualTable<TranshistoryWithCardInfo> {

    private static <T1, T2, T3> Function<T1, T3> chain(Function<T1, T2> function1, Function<T2, T3> function2) {
        return (T1 input) -> function2.apply(function1.apply(input));
    }

    public HistoryVisualTable(TableView<TranshistoryWithCardInfo> tableView) {
        super(tableView, ImmutableList.of(
                new IntegerVisualColumn<>("ID",  chain(TranshistoryWithCardInfo::getCard, CardsRecord::getId)),
                new StringVisualColumn<>("姓名", chain(TranshistoryWithCardInfo::getCard, CardsRecord::getName)),
                new FloatVisualColumn<>("积分变动", chain(TranshistoryWithCardInfo::getTranshistory, TranshistoryRecord::getCreditchange)),
                new StringVisualColumn<>("时间", chain(TranshistoryWithCardInfo::getTranshistory, TranshistoryRecord::getTime)),
                new StringVisualColumn<>("备注", chain(TranshistoryWithCardInfo::getTranshistory, TranshistoryRecord::getComment))));
    }
}