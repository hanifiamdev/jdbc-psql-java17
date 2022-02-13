package com.hanifiamdev.jdbc.dao.perpustakaan;

import com.hanifiamdev.jdbc.dao.CrudRepository;
import com.hanifiamdev.jdbc.entity.perpustakaan.Buku;
import com.hanifiamdev.jdbc.entity.perpustakaan.Penerbit;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PenerbitDao implements CrudRepository<Penerbit, String> {

    private Connection connection;

    public PenerbitDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Penerbit save(Penerbit value) throws SQLException {
        //language=PostgreSQL
        String query = "insert into perpustakaan.penerbit (nama, alamat) values (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, value.getNama());
        preparedStatement.setString(2, value.getAlamat());
        int row = preparedStatement.executeUpdate();
        log.info("Success Add Penerbit {} row ", row);
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            value.setId(generatedKeys.getString("id"));
        preparedStatement.close();
        return value;
    }

    @Override
    public Penerbit update(Penerbit value) throws SQLException {
        //language=PostgreSQL
        String query = "update perpustakaan.penerbit set nama = ?, alamat = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value.getNama());
        preparedStatement.setString(2, value.getAlamat());
        preparedStatement.setString(3, value.getId());
        int row = preparedStatement.executeUpdate();
        log.info("Success updated Penerbit {} row", row);
        preparedStatement.close();
        return value;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "delete from perpustakaan.penerbit where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value);
        int row = preparedStatement.executeUpdate();
        log.info("Row deleted Penerbit is {}", row);
        preparedStatement.close();
        return row >= 1;
    }

    @Override
    public Optional<Penerbit> findById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select p.id as id, \n" +
                "       p." +
                "nama as nama, \n" +
                "       p." +
                "alamat as alamat,\n" +
                "       b.id as buku_id,\n" +
                "       b.nama as buku_nama, \n" +
                "       b.isbn as buku_isbn, \n" +
                "       b.tanggal_terbit as tanggal_terbit \n" +
                "from perpustakaan.penerbit p\n" +
                "join perpustakaan.buku b on p.id = b.penerbit_id" +
                " \n" +
                "where p.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value);
        ResultSet resultSet = preparedStatement.executeQuery();
        Penerbit penerbit = new Penerbit();
        List<Buku> listBuku = new ArrayList<>();
        while (resultSet.next()) {
            Buku buku = new Buku();
            buku.setId(resultSet.getString("buku_id"));
            buku.setNama(resultSet.getString("buku_nama"));
            buku.setIsbn(resultSet.getString("buku_isbn"));
            listBuku.add(buku);
            if (penerbit.getId() == null) {
                penerbit.setId(resultSet.getString("id"));
                penerbit.setNama(resultSet.getString("nama"));
                penerbit.setAlamat(resultSet.getString("alamat"));
            }
        }
        penerbit.setListBuku(listBuku);
        resultSet.close();
        preparedStatement.close();
        return Optional.of(penerbit);
    }

    public List<Buku> findByPenerbitId(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select b." +
                "id             as buku_id,\n" +
                "       b.nama           as buku_nama,\n" +
                "       b.isbn           as buku_isbn,\n" +
                "       b.tanggal_terbit as tanggal_terbit\n" +
                "from perpustakaan.penerbit p\n" +
                "join perpustakaan.buku b on p.id = b.penerbit_id\n" +
                "where p.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value);
        ResultSet resultSet = preparedStatement.executeQuery();
        Penerbit penerbit = new Penerbit();
        List<Buku> listBuku = new ArrayList<>();
        while (resultSet.next()) {
            Buku buku = new Buku();
            buku.setId(resultSet.getString("buku_id"));
            buku.setNama(resultSet.getString("buku_nama"));
            buku.setIsbn(resultSet.getString("buku_isbn"));
            listBuku.add(buku);
        }
        preparedStatement.close();
        resultSet.close();
        return listBuku;
    }

    @Override
    public List<Penerbit> findAll() throws SQLException {
        List<Penerbit> list = new ArrayList<>();
        String query = "select id as id, " +
                "nama as nama, " +
                "alamat as alamat " +
                "from perpustakaan.penerbit";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Penerbit data = new Penerbit(
                    resultSet.getString("id"),
                    resultSet.getString("nama"),
                    resultSet.getString("alamat"),
                    this.findByPenerbitId(resultSet.getString("id"))
            );
            list.add(data);
        }
        resultSet.close();
        statement.close();
        return list;
    }

    @Override
    public List<Penerbit> findAll(Long start, Long limit, String orderDirection, Penerbit param) throws SQLException {
        return null;
    }
}
