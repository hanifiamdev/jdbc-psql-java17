package com.hanifiamdev.jdbc;

import com.hanifiamdev.jdbc.dao.perpustakaan.AnggotaDao;
import com.hanifiamdev.jdbc.dao.perpustakaan.BukuDao;
import com.hanifiamdev.jdbc.dao.perpustakaan.PenerbitDao;
import com.hanifiamdev.jdbc.dao.perpustakaan.TransaksiDao;
import com.hanifiamdev.jdbc.entity.perpustakaan.*;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Slf4j
public class App {

    private static DataSource dataSource;

    static {
        dataSource = new DatasourceConfig().getDataSource();
    }

    // mvn clean compile  -Dflyway.schemas=perpustakaan  flyway:migrate
    public static void testHapusPenerbit() {
        try (Connection connection = dataSource.getConnection()) {
            PenerbitDao dao = new PenerbitDao(connection);
            dao.removeById("001");

            // Check id
            Optional<Penerbit> byId = dao.findById("001");
            log.info("Apakah id 001 ada : {}", byId.isPresent());


        } catch (SQLException ex) {
            log.error("can't delete data", ex);
        }
    }

    public static void testSaveTransaksi() {
        try (Connection connection = dataSource.getConnection()) {
            log.info("status connected");
            connection.setAutoCommit(false);

            AnggotaDao anggotaDao = new AnggotaDao(connection);
            Optional<Anggota> hanif = anggotaDao.findById("001");

            BukuDao bukuDao = new BukuDao(connection);
            Optional<Buku> java1 = bukuDao.findById("001");
            Optional<Buku> java2 = bukuDao.findById("003");

            LocalDate tanggalPinjam = LocalDate.now();
            LocalDate tanggalKembali = tanggalPinjam.plusWeeks(1);

            TransaksiDao dao = new TransaksiDao(connection);

            Transaksi pinjamJava = new Transaksi(
                    null,
                    java.sql.Date.valueOf(tanggalPinjam),
                    hanif.orElse(null),
                    Arrays.asList(
                            new TransaksiDetail(
                                    null,
                                    null,
                                    java1.orElse(null),
                                    java.sql.Date.valueOf(tanggalKembali),
                                    null,
                                    null
                            ),
                            new TransaksiDetail(
                                    null,
                                    null,
                                    java2.orElse(null),
                                    java.sql.Date.valueOf(tanggalKembali),
                                    null,
                                    null
                            )
                    )

            );
            pinjamJava = dao.save(pinjamJava);
            connection.commit();

            Optional<Transaksi> dataSaved = dao.findById(pinjamJava.getId());
            if (dataSaved.isPresent()) {
                Transaksi transaksi = dataSaved.get();
                log.info("data berhasil {}", transaksi);
                log.info("data detail transaksi {}", transaksi.getDetails());
            }

            connection.close();


        } catch (SQLException ex) {
            log.error("can't delete data", ex);
        }
    }

    public static void testUpdateTransaksi() {
        try (Connection connection = dataSource.getConnection()) {
            TransaksiDao dao = new TransaksiDao(connection);
            Optional<TransaksiDetail> byTransactionIdAndBookId = dao.findByTransactionIdAndBookId("41ce6dd2-d23a-410a-98e0-de1570f930d7", "001");
            dao.update(byTransactionIdAndBookId.orElse(null));

        } catch (SQLException ex) {
            log.error("can't update data", ex);
        }
    }

    public static void testHapusTransaksi() {
        try (Connection connection = dataSource.getConnection()) {
            TransaksiDao dao = new TransaksiDao(connection);
            dao.removeById("41ce6dd2-d23a-410a-98e0-de1570f930d7");
            Optional<Transaksi> removeId = dao.findById("41ce6dd2-d23a-410a-98e0-de1570f930d7");
            log.info("removed : ", removeId.isPresent());

        } catch (SQLException ex) {
            log.error("can't delete data transaksi", ex);
        }
    }

    public static void main(String[] args) throws SQLException {
        //testHapusTransaksi();
         testSaveTransaksi();
    }
}
