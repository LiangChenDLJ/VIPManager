package com.chen.zhou.liang.lancet.model;

import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.google.common.collect.ImmutableList;
import javafx.scene.control.TableView;

public class CardsVisualTable extends VisualTable<CardsRecord> {
    public CardsVisualTable(TableView<CardsRecord> tableView) {
        super(tableView, ImmutableList.of(
                new IntegerVisualColumn<>("ID", CardsRecord::getId),
                new StringVisualColumn<>("姓名", CardsRecord::getName),
                new StringVisualColumn<>("身份证", CardsRecord::getIdcard),
                new StringVisualColumn<>("手机号", CardsRecord::getPhone),
                new FloatVisualColumn<>("积分", CardsRecord::getCredit)));
    }
}
