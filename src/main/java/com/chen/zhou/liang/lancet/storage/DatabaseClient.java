package com.chen.zhou.liang.lancet.storage;

import com.chen.zhou.liang.lancet.model.pojo.TranshistoryWithCardInfo;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.CardsRecord;
import com.chen.zhou.liang.lancet.storage.orm.tables.records.TranshistoryRecord;
import com.chen.zhou.liang.lancet.utils.TimeUtils;
import com.google.inject.Inject;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.chen.zhou.liang.lancet.storage.orm.Tables.CARDS;
import static com.chen.zhou.liang.lancet.storage.orm.Tables.TRANSHISTORY;

public class DatabaseClient {
    private final DSLContext dslContext;

    @Inject
    public DatabaseClient(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    private Condition GetCardQueryCondition(int id) {
        Condition condition = CARDS.ID.equal(id);
        return condition;
    }

    private Condition GetCardQueryConditionWildmatch(String wildcardQueryString) {
        final String likePhrase = "%" + wildcardQueryString + "%";
        Condition condition = CARDS.NAME.like(likePhrase).or(CARDS.IDCARD.like(likePhrase)).or(CARDS.PHONE.like(likePhrase)).or(CARDS.ID.like(likePhrase));
        try {
            int intVal = Integer.parseInt(wildcardQueryString);
            condition.or(CARDS.ID.eq(intVal));
        } catch (NumberFormatException ignored) {
        }
        return condition;
    }

    public List<CardsRecord> QueryCardsRecord(String wildcardQueryString) {
        return dslContext.selectFrom(CARDS).where(GetCardQueryConditionWildmatch(wildcardQueryString)).fetch();
    }

    public List<TranshistoryWithCardInfo> QueryTranshistoryAndCardsRecord(
            int cardId,
            Optional<LocalDateTime> startDateTime,
            Optional<LocalDateTime> endDateTime) {
        Condition condition = GetCardQueryCondition(cardId);
        if (startDateTime.isPresent()) {
            condition = condition.and(TRANSHISTORY.TIME.ge(TimeUtils.getTimeAsText(startDateTime.get())));
        }
        if (endDateTime.isPresent()) {
            condition = condition.and(TRANSHISTORY.TIME.le(TimeUtils.getTimeAsText(endDateTime.get())));
        }

        return dslContext.select().from(CARDS.join(TRANSHISTORY).on(CARDS.ID.eq(TRANSHISTORY.ID)))
                        .where(condition)
                        .fetch(record ->
                                TranshistoryWithCardInfo.builder()
                                        .card(record.into(CARDS).into(CardsRecord.class))
                                        .transhistory(record.into(TRANSHISTORY).into(TranshistoryRecord.class))
                                        .build()
                );
    }
}
