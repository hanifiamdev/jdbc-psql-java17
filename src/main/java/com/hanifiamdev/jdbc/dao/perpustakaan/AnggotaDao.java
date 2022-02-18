package com.hanifiamdev.jdbc.dao.perpustakaan;

import com.hanifiamdev.jdbc.dao.CrudRepository;
import com.hanifiamdev.jdbc.entity.perpustakaan.Anggota;
import com.hanifiamdev.jdbc.entity.perpustakaan.Buku;
import com.hanifiamdev.jdbc.entity.perpustakaan.Penerbit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnggotaDao implements CrudRepository<Anggota, String> {

    private Connection connection;

    public AnggotaDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Anggota save(Anggota value) throws SQLException {
        return null;
    }

    @Override
    public Anggota update(Anggota value) throws SQLException {
        return null;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        return null;
    }

    @Override
    public Optional<Anggota> findById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select id as id, nomor_ktp as ktp, nama as nama, alamat as alamat\n" +
                "from perpustakaan.anggota\n" +
                "where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            preparedStatement.close();
            resultSet.close();
            return Optional.empty();
        }
        Anggota anggota = new Anggota(
                resultSet.getString("id"),
                resultSet.getString("ktp"),
                resultSet.getString("nama"),
                resultSet.getString("alamat")
        );
        resultSet.close();
        preparedStatement.close();
        return Optional.of(anggota);
    }

    @Override
    public List<Anggota> findAll() throws SQLException {
        return null;
    }

    @Override
    public List<Anggota> findAll(Long start, Long limit, String orderDirection, Anggota param) throws SQLException {
        return null;
    }
}
