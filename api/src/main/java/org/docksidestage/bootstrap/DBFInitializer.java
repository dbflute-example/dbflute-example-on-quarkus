package org.docksidestage.bootstrap;

import org.docksidestage.dbflute.allcommon.DBFluteModule;
import org.docksidestage.di.GuiceComponents;
import org.jboss.logging.Logger;

import com.google.inject.Guice;

import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class DBFInitializer {

    Logger logger;

    AgroalDataSource dataSource;

    @Inject
    public DBFInitializer(Logger logger, AgroalDataSource dataSource) {
        this.logger = logger;
        this.dataSource = dataSource;
    }

    void onStart(@Observes StartupEvent ev) {
        // DBFluteのDI設定
        GuiceComponents.acceptInjector(Guice.createInjector(new DBFluteModule(dataSource)));
    }
}