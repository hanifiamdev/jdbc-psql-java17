package com.hanifiamdev.jdbc;

import com.hanifiamdev.jdbc.dao.ExampleTableDao;
import com.hanifiamdev.jdbc.entity.ExampleTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void testFindById() {
        try (Connection connection = this.dataSource.getConnection()) {
            this.dao = new ExampleTableDao(connection);

            Optional<ExampleTable> existId = this.dao.findById("001");
            Assertions.assertTrue(existId.isPresent(), "Data Tidak ditemukan");
            ExampleTable id001 = existId.get();
            Assertions.assertEquals(id001.getName(), "Hanif Amrulalah");

            Optional<ExampleTable> notExistId = this.dao.findById("006");
            Assertions.assertFalse(notExistId.isPresent(), "find by id 006 data not exist actualy");
        } catch (SQLException ex) {
            log.error("can't fetch data", ex);
        }

    }

    @Test
    public void testFindByIds() {
        try (Connection connection = this.dataSource.getConnection()) {
            this.dao = new ExampleTableDao(connection);
            List<ExampleTable> list = this.dao.findByIds("001", "002", "003");
            for (ExampleTable exampleTable : list) {
                log.info(exampleTable.toString());
            }
            Assertions.assertEquals(3, list.size(), "Jumlah data by ids");
        } catch (SQLException ex) {
            log.error("can't fetch data", ex);
        }

    }

    @Test
    public void testFindByListId() {
        try (Connection connection = this.dataSource.getConnection()) {
            this.dao = new ExampleTableDao(connection);
            List<ExampleTable> list = this.dao.findByIds(Arrays.asList("001", "002", "003"));
            for (ExampleTable exampleTable : list) {
                log.info(exampleTable.toString());
            }
            Assertions.assertEquals(3, list.size(), "Jumlah data List by id");
        } catch (SQLException ex) {
            log.error("can't fetch data", ex);
        }

    }

    @Test
    public void testPagination() {
        try (Connection connection = this.dataSource.getConnection()) {
            this.dao = new ExampleTableDao(connection);
            ExampleTable params = new ExampleTable();
            params.setActive(true);
            List<ExampleTable> datas = this.dao.findAll(0L, 2L, "createdtime", params);
            for (ExampleTable data : datas) {
                log.info("Data Page Pertama : {} ", data.toString());
            }
            Assertions.assertEquals(2, datas.size());
            List<ExampleTable> datas2 = this.dao.findAll(2L, 3L, "createdtime", params);
            for (ExampleTable data : datas2) {
                log.info("Data Page Kedua : {} ", data.toString());
            }
            Assertions.assertEquals(3, datas2.size());
        } catch (SQLException ex) {
            log.error("can't fetch data", ex);
        }

    }

    @Test
    void testUpdateData() {
        try (Connection connection = this.dataSource.getConnection()) {
            this.dao = new ExampleTableDao(connection);
            ExampleTable params = new ExampleTable("003", "Pratama Arhan", Date.valueOf(LocalDate.now()),
                    Timestamp.valueOf(LocalDateTime.now()), true, 0L, BigDecimal.ONE, null, 0f);
            ExampleTable exampleTable = this.dao.update(params);
            log.info(exampleTable.toString());
            Assertions.assertEquals("Pratama Arhan", exampleTable.getName());

        } catch (SQLException ex) {
            log.error("can't update data", ex);
        }
    }

    @Test
    void testDeleteData() {
        try (Connection connection = this.dataSource.getConnection()) {
            this.dao = new ExampleTableDao(connection);
            Boolean result = this.dao.removeById("005");
            log.info("Deleted Success is {}", result);
            Assertions.assertFalse(result, "Failed Deleted ");
        } catch (SQLException ex) {
            log.error("can't update data", ex);
        }

    }

    @Test
    void testSaveData() {
        try (Connection connection = this.dataSource.getConnection()) {
            this.dao = new ExampleTableDao(connection);
            ExampleTable data = new ExampleTable(null, "Matesi Sulaiman", Date.valueOf(LocalDate.now()),
                    Timestamp.valueOf(LocalDateTime.now()), true, 0L, BigDecimal.ONE, null, 0f);

            data = this.dao.save(data);
            assertNotNull(data.getId(), "Example id save");
            // select
            Optional<ExampleTable> optional = dao.findById(data.getId());
            Assertions.assertTrue(optional.isPresent(), "Data telah disimpan");

            // delete data
            dao.removeById(data.getId());
            optional = dao.findById(data.getId());
            Assertions.assertFalse(optional.isPresent(), "Data telah dihapus");

            // update
            optional = dao.findById("003");
            Assertions.assertTrue(optional.isPresent(), "find by id 003 is present");
            data = optional.orElse(new ExampleTable());
            String oldName = "Pratama Arhan";
            Assertions.assertEquals(oldName, data.getName(), "Check Name For Id 003");

            data.setName("Arho");
            dao.update(data);

            Optional<ExampleTable> optionalUpdated = dao.findById(data.getId());
            ExampleTable newData = optionalUpdated.orElse(new ExampleTable());
            Assertions.assertEquals("Arho", newData.getName(), "isEqual For Name Updated");

            // supaya balik nilai semula agar di running ulang tidak error
            data.setName(oldName);
            dao.update(data);
        } catch (SQLException ex) {
            log.error("can't update data", ex);
        }
    }

    @Test
    void testTransacctionCommitRollback() {
        Connection connection = null;
        ExampleTable person = new ExampleTable(
                null,
                "Muhamad Purwadi",
                Date.valueOf(LocalDate.now()),
                Timestamp.valueOf(LocalDateTime.now()),
                true,
                0l,
                new BigDecimal(100000),
                "test data",
                0f);
        try {
            connection = this.dataSource.getConnection();
            log.info("status connected");
            connection.setAutoCommit(false);
            log.info("status connected");
            this.dao = new ExampleTableDao(connection);


            ExampleTable save1 = this.dao.save(person);
            assertNotNull("employee id not null", save1.getId());
            log.info("employee: {}", save1);

            person.setCurrency(new BigDecimal(10));
            ExampleTable save2 = this.dao.update(person);
            connection.commit(); // jika ini di take out maka tidak akan tercommit untuk prosese transactionalnyasactional
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            log.error("sql exception", exception);
            if (connection != null) {
                try {
                    connection.rollback();
                    log.warn("was rollback");
                    connection.close();
                } catch (SQLException sqlRollbackException) {
                    log.error("failed rollback", sqlRollbackException);
                }
            }
        }

        // Tes dengan membuka koneksi baru untuk mengecek data sudah masuk apa belum
        Connection connection2 = null;
        try {
            connection2 = this.dataSource.getConnection();
            ExampleTableDao dao = new ExampleTableDao(connection2);
            Optional<ExampleTable> newDataExist = dao.findById(person.getId());
            // Check apakah data baru sudah tersimpan ,,jka iya akan bernilai true
            Assertions.assertTrue(newDataExist.isPresent(), "Data Baru tersimpan?");
            if (newDataExist.isPresent()) {
                ExampleTable data = newDataExist.get();
                dao.removeById(data.getId());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    // BulkUpload sering disebut juga Batch Processing
    @Test
    void testBulkUplload() {
        Connection connection = null;
        List<ExampleTable> list = new ArrayList<>();
        List<String> newId = new ArrayList<>();
        try {
            connection = this.dataSource.getConnection();
            connection.setAutoCommit(false);

            list.add(new ExampleTable(
                    null,
                    "Indra Kenz",
                    Date.valueOf(LocalDate.now()),
                    Timestamp.valueOf(LocalDateTime.now()),
                    true,
                    0l,
                    new BigDecimal(200000),
                    "Affiliator Kondang",
                    0f));

            list.add(new ExampleTable(
                    null,
                    "Doni salmanan",
                    Date.valueOf(LocalDate.now()),
                    Timestamp.valueOf(LocalDateTime.now()),
                    true,
                    0l,
                    new BigDecimal(200000),
                    "Affiliator Kondang",
                    0f));
            this.dao = new ExampleTableDao(connection);
            newId = dao.save(list);
            Assertions.assertEquals(2, newId.size(), "Jumlah ada terinsert");

            connection.commit();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            log.error("sql exception", sqle);
            if (connection != null) {
                try {
                    connection.rollback();
                    log.warn("was rollback");
                    connection.close();
                } catch (SQLException sqlRollbackException) {
                    log.error("failed rollback", sqlRollbackException);
                }
            }
        }
        // Tes dengan membuka koneksi baru untuk mengecek data sudah masuk apa belum
        Connection connection2 = null;
        try {
            connection2 = this.dataSource.getConnection();
            ExampleTableDao dao = new ExampleTableDao(connection2);
            List<ExampleTable> newList = dao.findByIds(newId);

            Assertions.assertEquals(2, newList.size(), "Data Baru tersimpan jumlahnya");
            dao.removeById(newId);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }


    }
}
