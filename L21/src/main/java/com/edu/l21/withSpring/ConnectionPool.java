package com.edu.l21.withSpring;

public class ConnectionPool {
    private final DataSource dataSource;

    public ConnectionPool(DataSource dataSource) {
        this.dataSource = dataSource;
        System.out.println("ConnectionPool created with DataSource: " + dataSource.getUrl());
    }

    public String getConnection() {
        return "Connection from pool: " + dataSource.getUrl();
    }
}

