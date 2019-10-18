package com.csv.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Parsed {
}
