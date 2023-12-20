package com.chen.zhou.liang.lancet.model.pojo;

import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.TranshistoryRecord;
import lombok.Builder;
import lombok.Getter;

@Builder
public class TranshistoryWithCardInfo {
    @Getter
    private CardsRecord card;
    @Getter
    private TranshistoryRecord transhistory;
}
