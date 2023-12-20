package com.chen.zhou.liang.lancet.model;

import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.chen.zhou.liang.lancet.utils.DisplayableException;
import com.google.common.collect.ImmutableList;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class CardsUpdatePane {
    private final GridPane valueListPane;
    private final ImmutableList<VisualField<CardsRecord, ?>> fieldsList;

    public CardsUpdatePane(GridPane valueListPane, CardsRecord cardsRecord) {
        IntegerVisualField<CardsRecord> idField =
                new IntegerVisualField<>("ID", CardsRecord::setId, true);
        idField.setValue(cardsRecord.getId());
        idField.getTextField().setEditable(false);

        StringVisualField<CardsRecord> nameField =
                new StringVisualField<>("姓名", CardsRecord::setName, true);
        nameField.setValue(cardsRecord.getName());

        StringVisualField<CardsRecord> idcardField =
                new StringVisualField<>("身份证", CardsRecord::setIdcard,false);
        idcardField.setValue(cardsRecord.getIdcard());

        StringVisualField<CardsRecord> phoneField =
                new StringVisualField<>("手机号", CardsRecord::setPhone,false);
        phoneField.setValue(cardsRecord.getPhone());

        this.valueListPane = valueListPane;
        fieldsList = ImmutableList.of(idField, nameField, idcardField, phoneField);
        for (int i = 0; i < fieldsList.size(); i++) {
            fieldsList.get(i).addRowToPane(valueListPane, i);
        }
    }

    public void fillCardsRecord(CardsRecord cardsRecord) throws DisplayableException{
        for (VisualField<CardsRecord, ?> field : fieldsList) {
            field.fillValue(cardsRecord);
        }
    }
}
