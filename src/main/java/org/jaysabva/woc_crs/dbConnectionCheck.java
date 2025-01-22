package org.jaysabva.woc_crs;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class dbConnectionCheck implements ApplicationRunner {

    private final DataSource dataSource;

    public dbConnectionCheck(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Connecting to the database...");
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && connection.isValid(2)) {
                System.out.println("Successfully connected to the database!");
                System.out.println("Database URL: " + connection.getMetaData().getURL());
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
        }
    }
}