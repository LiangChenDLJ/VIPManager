package com.chen.zhou.liang.lancet.model;

import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.chen.zhou.liang.lancet.utils.DisplayableException;
import com.google.common.collect.ImmutableList;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class CardsRegisterPane {
    private final GridPane valueListPane;
    private final ImmutableList<VisualField<CardsRecord, ?>> fieldsList;

    public CardsRegisterPane(GridPane valueListPane) {
        this.valueListPane = valueListPane;
        fieldsList = ImmutableList.of(
                new IntegerVisualField<>("ID", CardsRecord::setId, true),
                new StringVisualField<>("姓名", CardsRecord::setName, true),
                new StringVisualField<>("身份证", CardsRecord::setIdcard,false),
                new StringVisualField<>("手机号", CardsRecord::setPhone,false));
        for (int i = 0; i < fieldsList.size(); i++) {
            fieldsList.get(i).addRowToPane(valueListPane, i);
        }
    }

    public void fillCardsRecord(CardsRecord cardsRecord)throws DisplayableException{
        for (VisualField<CardsRecord, ?> field : fieldsList) {
            field.fillValue(cardsRecord);
        }
    }
}
