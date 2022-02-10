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
public class BukuDao implements CrudRepository<Buku, String> {

    private Connection connection;

    public BukuDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Buku save(Buku value) throws SQLException {
        //language=PostgreSQL
        String query = "insert into perpustakaan.buku (nama, isbn, penerbit_id, tanggal_terbit) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, value.getNama());
        preparedStatement.setString(2, value.getIsbn());
        if (value.getPenerbit() != null) {
            preparedStatement.setNull(3, Types.VARCHAR);
        } else {
            preparedStatement.setString(3, value.getPenerbit().getId());
        }
        int row = preparedStatement.executeUpdate();
        log.info("Success Add Buku {} row ", row);
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            value.setId(generatedKeys.getString("id"));
        preparedStatement.close();
        return value;
    }

    @Override
    public Buku update(Buku value) throws SQLException {
        //language=PostgreSQL
        String query = "update perpustakaan.buku set nama = ?, isbn = ?, penerbit_id = ?, tanggal_terbit = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value.getNama());
        preparedStatement.setString(2, value.getIsbn());
        if (value.getPenerbit() != null) {
            preparedStatement.setNull(3, Types.VARCHAR);
        } else {
            preparedStatement.setString(3, value.getPenerbit().getId());
        }
        preparedStatement.setDate(4, (Date) value.getTangggalTerbit() != null ? (Date) value.getTangggalTerbit() : null);
        preparedStatement.setString(5, value.getId());
        int row = preparedStatement.executeUpdate();
        log.info("Success Update Buku {} row ", row);
        preparedStatement.close();
        return value;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "delete from perpustakaan.buku where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value);
        int row = preparedStatement.executeUpdate();
        log.info("Success Deleted Buku {} row ", row);
        preparedStatement.close();
        return row >= 1;
    }

    @Override
    public Optional<Buku> findById(String value) throws SQLException {
        String query = "select b.id             as bukuId,\n" +
                "       b.nama           as bukuName,\n" +
                "       b.isbn           as isbn,\n" +
                "       p.id             as penerbitId,\n" +
                "       p.nama           as penerbitName,\n" +
                "       p.alamat         as penerbitAlamat,\n" +
                "       " +
                "b.tanggal_terbit as tanggalTerbit\n" +
                "from perpustakaan.buku b\n" +
                "         left join perpustakaan.penerbit p on (b.penerbit_id = p.id)" +
                "where b.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            preparedStatement.close();
            return Optional.empty();
        }

        Buku data = new Buku(
                resultSet.getString("bukuId"),
                resultSet.getString("bukuName"),
                resultSet.getString("isbn"),
                new Penerbit(
                        resultSet.getString("penerbitId"),
                        resultSet.getString("penerbitName"),
                        resultSet.getString("penerbitAlamat"),
                        new ArrayList<>()
                ),
                resultSet.getDate("tanggalTerbit")
        );
        resultSet.close();
        preparedStatement.close();
        return Optional.of(data);
    }

    @Override
    public List<Buku> findAll() throws SQLException {
        List<Buku> list = new ArrayList<>();
        //language=PostgreSQL
        String query = "select b.id             as bukuId,\n" +
                "       b.nama           as bukuName,\n" +
                "       b.isbn           as isbn,\n" +
                "       p.id             as penerbitId,\n" +
                "       p.nama           as penerbitName,\n" +
                "       p.alamat         as penerbitAlamat,\n" +
                "       " +
                "b.tanggal_terbit as tanggalTerbit\n" +
                "from perpustakaan.buku b\n" +
                "         left join perpustakaan.penerbit p on (b.penerbit_id = p.id)";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Buku data = new Buku(
                    resultSet.getString("bukuId"),
                    resultSet.getString("bukuName"),
                    resultSet.getString("isbn"),
                    new Penerbit(
                            resultSet.getString("penerbitId"),
                            resultSet.getString("penerbitName"),
                            resultSet.getString("penerbitAlamat"),
                            new ArrayList<>()
                    ),
                    resultSet.getDate("tanggalTerbit")
            );
            list.add(data);
        }
        resultSet.close();
        statement.close();
        return list;
    }

    @Override
    public List<Buku> findAll(Long start, Long limit, String orderDirection, Buku param) throws SQLException {
        return null;
    }
}
