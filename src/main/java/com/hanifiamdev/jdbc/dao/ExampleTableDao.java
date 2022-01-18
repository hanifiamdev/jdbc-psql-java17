package com.hanifiamdev.jdbc.dao;

import com.hanifiamdev.jdbc.entity.ExampleTable;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ExampleTableDao implements CrudRepository<ExampleTable, String> {

    private Connection connection;

    public ExampleTableDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ExampleTable save(ExampleTable value) throws SQLException {
        String query = "insert into example_table (id, name, created_date, created_time, is_active, counter, currency, description, floating" +
                ")\n" +
                "values (gen_random_uuid(), ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, value.getName());
        preparedStatement.setDate(2, value.getCreatedDate());
        preparedStatement.setTimestamp(3, value.getCreatedTime());
        preparedStatement.setBoolean(4, value.getActive());
        preparedStatement.setLong(5, value.getCounter());
        preparedStatement.setBigDecimal(6, value.getCurrency());
        preparedStatement.setString(7, value.getDescription());
        preparedStatement.setFloat(8, value.getFloating());
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            value.setId(generatedKeys.getString("id")); // bisa diisi 1, karena kolom ke - 1 untuk id
        preparedStatement.close();
        return value;
    }

    @Override
    public ExampleTable update(ExampleTable value) throws SQLException {
        String query = "update example_table\n" +
                "set name        = ?,\n" +
                "    is_active   = ?,\n" +
                "    counter     = ?,\n" +
                "    currency     = ?,\n" +
                "    description = ?,\n" +
                "    floating    = ?\n" +
                "where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value.getName());
        preparedStatement.setBoolean(2, value.getActive());
        preparedStatement.setLong(3, value.getCounter());
        preparedStatement.setBigDecimal(4, value.getCurrency());
        preparedStatement.setString(5, value.getDescription());
        preparedStatement.setFloat(6, value.getFloating());
        preparedStatement.setString(7, value.getId());
        int row = preparedStatement.executeUpdate();
        log.info("Success {} row Updated", row);
        preparedStatement.close();
        return value;
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        String query = "delete\n" +
                "from example_table\n" +
                "where id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, value);
        int row = statement.executeUpdate();
        statement.close();
        return row >= 1;
    }

    @Override
    public Optional<ExampleTable> findById(String id) throws SQLException {
        String query = "select id    as id,\n" +
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

    public List<ExampleTable> findByIds(String... ids) throws SQLException {
        List<ExampleTable> list = new ArrayList<>();
        String query = "select id    as id,\n" +
                "       name         as name,\n" +
                "       created_date as createdDate,\n" +
                "       created_time as createdTime,\n" +
                "       is_active    as active,\n" +
                "       counter      as counter,\n" +
                "       currency     as currency,\n" +
                "       description  as description,\n" +
                "       floating     as floating\n" +
                "from example_table\n" +
                "where id = any (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        Array arrayOfId = connection.createArrayOf("VARCHAR", ids.clone());
        preparedStatement.setArray(1, arrayOfId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ExampleTable data;
        while (resultSet.next()) {
            data = new ExampleTable(
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
        preparedStatement.close();
        arrayOfId.free();
        return list;
    }

    public List<ExampleTable> findByIds(List<String> param) throws SQLException {
        List<ExampleTable> list = new ArrayList<>();
        String query = "select id    as id,\n" +
                "       name         as name,\n" +
                "       created_date as createdDate,\n" +
                "       created_time as createdTime,\n" +
                "       is_active    as active,\n" +
                "       counter      as counter,\n" +
                "       currency     as currency,\n" +
                "       description  as description,\n" +
                "       floating     as floating\n" +
                "from example_table\n" +
                "where id = any (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        Array arrayOfId = connection.createArrayOf("VARCHAR", param.toArray());
        preparedStatement.setArray(1, arrayOfId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ExampleTable data;
        while (resultSet.next()) {
            data = new ExampleTable(
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
        preparedStatement.close();
        arrayOfId.free();
        return list;
    }

    @Override
    public List<ExampleTable> findAll() throws SQLException {
        List<ExampleTable> list = new ArrayList<>();
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
