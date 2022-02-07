package com.hanifiamdev.jdbc.dao.perpustakaan;

import com.hanifiamdev.jdbc.dao.CrudRepository;
import com.hanifiamdev.jdbc.entity.ExampleTable;
import com.hanifiamdev.jdbc.entity.perpustakaan.Penerbit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PenerbitDao implements CrudRepository<Penerbit, String> {

    private Connection connection;

    public PenerbitDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Penerbit save(Penerbit value) throws SQLException {
        return null;
    }

    @Override
    public Penerbit update(Penerbit value) throws SQLException {
        return null;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        return null;
    }

    @Override
    public Optional<Penerbit> findById(String value) throws SQLException {
        String query = "select id as id, " +
                "nama as nama, " +
                "alamat as alamat " +
                "from perpustakaan.penerbit " +
                "where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            preparedStatement.close();
            return Optional.empty();
        }

        Penerbit data = new Penerbit(
                resultSet.getString("id"),
                resultSet.getString("nama"),
                resultSet.getString("alamat"),
                new ArrayList<>()
        );
        resultSet.close();
        preparedStatement.close();
        return Optional.of(data);
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
                    new ArrayList<>()
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
