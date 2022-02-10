package com.hanifiamdev.jdbc;

import com.hanifiamdev.jdbc.dao.perpustakaan.PenerbitDao;
import com.hanifiamdev.jdbc.entity.perpustakaan.Penerbit;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class App {

    private static DataSource dataSource;

    static {
        dataSource = new DatasourceConfig().getDataSource();
    }

    public static void main(String[] args) {
        testHapusPenerbit();
        System.out.println("Hello Java 17 dan PostgreSql 14.1");
        /*
         * NO ACTION
         *   Akan error constraints jika mempunyai relasi, tapi tidak akan dipedulikan jika
         *             tidak punya relasi
         * RESTRICT
         *   Akan error constraints jika mempunyai relasi, tapi tidak akan dipedulikan jika
         *             tidak punya relasi
         * SET NULL
         *   tidak eror jika di table parent di hapus dan akan set null untuk table child yang berelasi
         *             ( kolom reference tidak boleh ter set not null )
         *
         *
         * */
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
}
