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
            log.info("=== Result testFindAllData ===");
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
            this.dao = new ExampleTableDao(connection);

            Optional<ExampleTable> existId = this.dao.findById("001");
            Assertions.assertTrue(existId.isPresent(), "Data Tidak ditemukan");
            ExampleTable id001 = existId.get();
            Assertions.assertEquals(id001.getName(), "Hanif Amrulalah");

            Optional<ExampleTable> notExistId = this.dao.findById("006");
            Assertions.assertFalse( notExistId.isPresent(), "find by id 006 data not exist actualy");
        } catch (SQLException ex) {
            log.error("can't fetch data", ex);
        }

    }

    @Test
    public void testPagination() throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            this.dao = new ExampleTableDao(connection);
            ExampleTable params = new ExampleTable();
            params.setActive(true);
            List<ExampleTable> datas = this.dao.findAll(0L,2L, "createdtime", params);
            for (ExampleTable data : datas) {
                log.info("Data Page Pertama : {} ",  data.toString());
            }
            Assertions.assertEquals(2, datas.size());
            List<ExampleTable> datas2 = this.dao.findAll(2L,3L, "createdtime", params);
            for (ExampleTable data : datas2) {
                log.info("Data Page Kedua : {} ",  data.toString());
            }
            Assertions.assertEquals(3, datas2.size());
        } catch (SQLException ex) {
            log.error("can't fetch data", ex);
        }

    }
}
