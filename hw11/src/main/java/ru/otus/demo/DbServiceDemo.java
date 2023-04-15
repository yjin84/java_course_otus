package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws InterruptedException {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var myCache = new MyCache<String, Client>();

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        var dbServiceClientWithCache = new DbServiceClientImpl(transactionManager, clientTemplate, myCache);

        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
///
        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated"));
        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        var clients = dbServiceClient.findAll();
        clients.forEach(client -> log.info("client:{}", client));

        var clientsSize = clients.size();

        var startTime = System.currentTimeMillis();
        getClients(dbServiceClient, clientsSize);
        var timeWithoutCache = System.currentTimeMillis() - startTime;

        // Прогрев кэша
        getClients(dbServiceClientWithCache, clientsSize);

        startTime = System.currentTimeMillis();
        getClients(dbServiceClientWithCache, clientsSize);
        var timeWithCache = System.currentTimeMillis() - startTime;

        log.info("Without cache: {}", timeWithoutCache);
        log.info("With cache: {}", timeWithCache);

        System.gc();
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));

        log.info("After call gc");
        getClients(dbServiceClientWithCache, clientsSize);
    }

    private static void getClients(DbServiceClientImpl dbServiceClient, int size) {
        for (int i = 1; i < size + 1; i++) {
            dbServiceClient.getClient(i);
        }
    }
}
