package com.chen.zhou.liang.lancet;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Annotations {
    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface PrimaryStage {}

    private Annotations() {}
}
