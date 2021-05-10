package com.chen.zhou.liang.lancet.storage;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.sqlite.SQLiteDataSource;

public class StorageModule extends AbstractModule {
    private static final String DATABASE_URL = "jdbc:sqlite:data/VIPManager.db";

    @Override
    protected void configure() {
        bind(Authenticator.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    DSLContext provideDSLContext() {
        SQLiteDataSource sqliteDataSource = new SQLiteDataSource();
        sqliteDataSource.setUrl(DATABASE_URL);
        return DSL.using(sqliteDataSource, SQLDialect.SQLITE);
    }
}
