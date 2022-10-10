package com.chen.zhou.liang.lancet.model;

import com.chen.zhou.liang.lancet.storage.orm.tables.records.TranshistoryRecord;
import com.google.common.collect.ImmutableList;
import javafx.scene.control.TableView;

public class HistoryVisualTable extends VisualTable<TranshistoryRecord> {
    public HistoryVisualTable(TableView<TranshistoryRecord> tableView) {
        super(tableView, ImmutableList.of(
                new StringVisualColumn<>("时间", TranshistoryRecord::getTime),
                new FloatVisualColumn<>("积分变动", TranshistoryRecord::getCreditchange),
                new StringVisualColumn<>("备注", TranshistoryRecord::getComment)));
    }
}