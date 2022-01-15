package com.hanifiamdev.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class DatasourceConfigTest{

    private  DatasourceConfig config;

    @BeforeEach
    void setUp() {
        this.config = new DatasourceConfig("jdbc:postgresql://localhost:5432/hr", "hr", "hr");
    }

    @Test
    void testConnectionToDb() throws SQLException {
        DataSource dataSource = this.config.getDataSource();
        Connection connection = dataSource.getConnection();
        log.info("Status Connected");
    }
}