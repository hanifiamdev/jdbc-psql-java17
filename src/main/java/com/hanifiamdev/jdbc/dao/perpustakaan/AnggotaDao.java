package com.hanifiamdev.jdbc.dao.perpustakaan;

import com.hanifiamdev.jdbc.dao.CrudRepository;
import com.hanifiamdev.jdbc.entity.perpustakaan.Anggota;

import java.sql.Connection;
import java.sql.SQLException;
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
        return Optional.empty();
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
