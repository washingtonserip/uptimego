package io.uptimego.utils;

import java.util.Locale;


public final class ProjectConstants {



    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final Locale BRAZIL_LOCALE = new Locale.Builder().setLanguage("pt").setRegion("BR").build();

    private ProjectConstants() {

        throw new UnsupportedOperationException();
    }

}