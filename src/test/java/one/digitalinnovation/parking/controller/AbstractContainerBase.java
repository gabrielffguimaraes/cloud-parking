package one.digitalinnovation.parking.controller;


import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractContainerBase {
    static protected MySQLContainer MY_SQL_CONTAINER = null;
    static {
        MY_SQL_CONTAINER = new MySQLContainer();
        MY_SQL_CONTAINER.start();
        System.setProperty("spring.datasource.url",MY_SQL_CONTAINER.getJdbcUrl());
        System.setProperty("spring.datasource.username",MY_SQL_CONTAINER.getUsername());
        System.setProperty("spring.datasource.password",MY_SQL_CONTAINER.getPassword());
        System.setProperty("spring.jpa.hibernate.ddl-auto","update");

    }
}
