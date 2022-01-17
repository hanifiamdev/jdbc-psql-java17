package com.hanifiamdev.jdbc.dao;

import com.hanifiamdev.jdbc.entity.ExampleTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExampleTableDao implements CrudRepository<ExampleTable, String> {

    private Connection connection;

    public ExampleTableDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ExampleTable save(ExampleTable value) throws SQLException {
        return null;
    }

    @Override
    public ExampleTable update(ExampleTable value) throws SQLException {
        return null;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        return null;
    }

    @Override
    public Optional<ExampleTable> findById(String id) throws SQLException {
        //language=PostgreSQL
        String query = "select id           as id,\n" +
                "       name         as name,\n" +
                "       created_date as createdDate,\n" +
                "       created_time as createdTime,\n" +
                "       is_active    as active,\n" +
                "       counter      as counter,\n" +
                "       currency     as currency,\n" +
                "       description  as description,\n" +
                "       floating     as floating\n" +
                "from example_table\n" +
                "where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            preparedStatement.close();
            return Optional.empty();
        }

        ExampleTable data = new ExampleTable(
                resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getDate("createdDate"),
                resultSet.getTimestamp("createdTime"),
                resultSet.getObject("active", Boolean.class),
                resultSet.getLong("counter"),
                resultSet.getBigDecimal("currency"),
                resultSet.getString("description"),
                resultSet.getFloat("floating")
        );
        resultSet.close();
        preparedStatement.close();
        return Optional.of(data);
    }

    @Override
    public List<ExampleTable> findAll() throws SQLException {
        List<ExampleTable> list = new ArrayList<>();
        //language=PostgreSQL
        String query = "select id as id, name as name," +
                "created_date as createdDate, " +
                "created_time as createdTime, " +
                "is_active    as active, " +
                "counter      as counter, " +
                "currency     as currency, " +
                "description  as description, " +
                "floating     as floating " +
                "from example_table";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            ExampleTable data = new ExampleTable(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("createdDate"),
                    resultSet.getTimestamp("createdTime"),
                    resultSet.getObject("active", Boolean.class),
                    resultSet.getLong("counter"),
                    resultSet.getBigDecimal("currency"),
                    resultSet.getString("description"),
                    resultSet.getFloat("floating")
            );
            list.add(data);
        }
        resultSet.close();
        statement.close();
        return list;
    }

    @Override
    public List<ExampleTable> findAll(Long start, Long limit, String orderDirection, ExampleTable param) throws SQLException {
        //language=PostgreSQL
        String query = "select id           as id,\n" +
                "       name         as name,\n" +
                "       created_date as createdDate,\n" +
                "       created_time as createdTime,\n" +
                "       is_active    as active,\n" +
                "       counter      as counter,\n" +
                "       currency     as currency,\n" +
                "       description  as description,\n" +
                "       floating     as floating\n" +
                "from example_table\n" +
                "where lower(name) like ? or is_active = ? \n" +
                "order by ? desc\n" +
                "limit ? offset ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, param.getName());
        statement.setBoolean(2, param.getActive());
        statement.setString(3, orderDirection);
        statement.setLong(4, limit);
        statement.setLong(5, start);
        // tampung result
        List<ExampleTable> list = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            ExampleTable data = new ExampleTable(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("createdDate"),
                    resultSet.getTimestamp("createdTime"),
                    resultSet.getObject("active", Boolean.class),
                    resultSet.getLong("counter"),
                    resultSet.getBigDecimal("currency"),
                    resultSet.getString("description"),
                    resultSet.getFloat("floating")
            );
            list.add(data);
        }
        statement.close();
        resultSet.close();
        return list;
    }
}
