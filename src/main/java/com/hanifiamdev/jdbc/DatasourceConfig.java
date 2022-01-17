package com.hanifiamdev.jdbc;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class DatasourceConfig {

    private final HikariConfig config;

    public DatasourceConfig() {
        Properties prop = new Properties();
        InputStream input = DatasourceConfig.class.getResourceAsStream("/application.properties");
        try {
            prop.load(input);
            input.close();
        } catch (IOException e) {
            log.error("can't load file property", e);
        }
        log.info("{} | {} | {} | {}",prop.getProperty("jdbc.url"), prop.getProperty("jdbc.username"),
                prop.getProperty("jdbc.password"), prop.getProperty("jdbc.schema"));
        this.config = new HikariConfig();
        config.setJdbcUrl(prop.getProperty("jdbc.url"));
        config.setUsername(prop.getProperty("jdbc.username"));
        config.setPassword(prop.getProperty("jdbc.password"));
        config.setSchema(prop.getProperty("jdbc.schema"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }



    public DataSource getDataSource() {
        return new HikariDataSource(config);
    }
}
