package com.hanifiamdev.jdbc;

import com.hanifiamdev.jdbc.dao.ExampleTableDao;
import com.hanifiamdev.jdbc.entity.ExampleTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ExampleTableTest {

    private DataSource dataSource;

    private ExampleTableDao dao;

    @BeforeEach
    void setUp() {
        this.dataSource = new DatasourceConfig().getDataSource();
    }

    @Test
    public void testFindAllData() {
        try (Connection connection = this.dataSource.getConnection()) {
            this.dao = new ExampleTableDao(connection);
            List<ExampleTable> list = this.dao.findAll();
            list.forEach(data -> {
                log.info("{}", data.toString());
            });
            Assertions.assertEquals(list.size(), 5);
        } catch (SQLException ex) {
            log.error("can't fetch data", ex);
        }
    }

    @Test
    public void testFindById() throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            ExampleTableDao dao = new ExampleTableDao(connection);
            Optional<ExampleTable> data = dao.findById("00'1"); // ini yang membuat eror karena ada ijection character '
            log.info("{}", data.isPresent());
        } catch (SQLException ex) {
            log.error("can't fetch data", ex);
        }

    }
}
