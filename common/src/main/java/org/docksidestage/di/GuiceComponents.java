package org.docksidestage.di;

import com.google.inject.Injector;

public class GuiceComponents {

    protected static Injector injector;

    public static void acceptInjector(Injector initializedInjector) {
        injector = initializedInjector;
    }

    public static <COMPONENT> COMPONENT find(Class<COMPONENT> type) {
        return injector.getInstance(type);
    }
}