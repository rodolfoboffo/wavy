package com.terpomo.wavy.marshal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MarshalAttr {
    String attrName();
}
