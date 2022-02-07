package com.hanifiamdev.jdbc;

import com.hanifiamdev.jdbc.dao.perpustakaan.PenerbitDao;
import com.hanifiamdev.jdbc.entity.ExampleTable;
import com.hanifiamdev.jdbc.entity.perpustakaan.Penerbit;
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
public class PenerbitTest {

    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        this.dataSource = new DatasourceConfig().getDataSource();
    }

    @Test
    public void testFindAll() {
        try (Connection connection = this.dataSource.getConnection()) {
            PenerbitDao dao = new PenerbitDao(connection);
            List<Penerbit> list = dao.findAll();
            log.info("=== Result testFindAllData ===");
            list.forEach(data -> {
                log.info("{}", data.toString());
            });
            Assertions.assertEquals(list.size(), 2);
        } catch (SQLException ex) {
            log.error("can't fetch data", ex);
        }
    }

    @Test
    public void testFindById() {
        try (Connection connection = this.dataSource.getConnection()) {
            PenerbitDao dao = new PenerbitDao(connection);
            Optional<Penerbit> byId = dao.findById("001");
            Assertions.assertTrue(byId.isPresent(), "Data tidak ditemukan");
            Penerbit penerbit = byId.get();
            Assertions.assertEquals(penerbit.getNama(), "Informatika", "Id Penerbit 001 adalah");

           Optional<Penerbit> notExistId = dao.findById("099");
            Assertions.assertFalse(notExistId.isPresent(), "find by id 006 data not exist actualy");

        } catch (SQLException ex) {
            log.error("can't fetch data", ex);
        }
    }
}
