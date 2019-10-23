package com.github.zieiony.guide;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityAnnotation {
    String title();
}
