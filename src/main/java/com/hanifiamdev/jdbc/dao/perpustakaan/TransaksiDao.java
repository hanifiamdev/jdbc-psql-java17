package com.hanifiamdev.jdbc.dao.perpustakaan;

import com.hanifiamdev.jdbc.dao.CrudRepository;
import com.hanifiamdev.jdbc.entity.perpustakaan.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransaksiDao implements CrudRepository<Transaksi, String> {

    private Connection connection;

    public TransaksiDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Transaksi save(Transaksi value) throws SQLException {
        String transaksiQuery = "insert into perpustakaan.transaksi(tanggal_pinjam, anggota_id) values(?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(transaksiQuery, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setDate(1, (Date) value.getCreateedDate());
        if (value.getAnggota() != null)
            preparedStatement.setString(2, value.getAnggota().getId());
        else
            preparedStatement.setNull(2, Types.VARCHAR);
        int row = preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            value.setId(generatedKeys.getString("id"));
        preparedStatement.close();

        String transaksiDetailQuery = "insert into perpustakaan.transaksi_detail(transaksi_id, buku_id, tanggal_kembali, is_return)\n" +
                "values (?, ?, ?, false)";
        preparedStatement = connection.prepareStatement(transaksiDetailQuery, Statement.RETURN_GENERATED_KEYS);
        for (TransaksiDetail detail : value.getDetails()) {
            preparedStatement.setString(1, value.getId());
            if (detail.getBuku() != null)
                preparedStatement.setString(2, detail.getBuku().getId());
            else
                preparedStatement.setNull(2, Types.VARCHAR);

            preparedStatement.setDate(3, (Date) detail.getTanggalKembali());
            preparedStatement.addBatch();
        }
        generatedKeys.close();
        preparedStatement.executeBatch();
        preparedStatement.close();
        return value;
    }

    @Override
    public Transaksi update(Transaksi value) throws SQLException {
        return null;
    }

    public TransaksiDetail update(TransaksiDetail value) throws SQLException {
        //language=PostgreSQL
        String query = "update perpustakaan.transaksi_detail\n" +
                "set is_return   = true,\n" +
                "    date_return = now()\n" +
                "where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value.getId());
        int row = preparedStatement.executeUpdate();
        preparedStatement.close();
        return value;
    }

    public Optional<TransaksiDetail> findByTransactionIdAndBookId(String transactionId, String bookId) throws SQLException {
        //language=PostgreSQL
        String query = "select trx_detail.id              as id,\n" +
                "trx_detail.buku_id         as buku_id,\n" +
                "trx_detail.tanggal_kembali as tanggal_kembali,\n" +
                "       trx_detail.is_return       as status_kembali,\n" +
                "       trx_detail.date_return     as last_updated_date,\n" +
                "       book.id                    as buku_id,\n" +
                "       book.nama                  as buku_name,\n" +
                "       book.isbn                  as buku_isbn,\n" +
                "       book.penerbit_id           as penerbit_id,\n" +
                "       book.tanggal_terbit        as buku_tanggal_terbit,\n" +
                "       pub.nama                   as penerbit_nama,\n" +
                "       pub.alamat                   as penerbit_alamat,\n" +
                "       trx.id                     as transactionId,\n" +
                "       trx.tanggal_pinjam         as transactionDate,\n" +
                "       trx.anggota_id             as anggotaId,\n" +
                "       agt.nomor_ktp              as anggotaKtp,\n" +
                "       agt.nama                   as anggotaNama,\n" +
                "       agt.alamat                 as anggotaAlamat\n" +
                "from perpustakaan.transaksi_detail trx_detail\n" +
                "left join perpustakaan.transaksi trx on trx.id = trx_detail.transaksi_id\n" +
                "left join perpustakaan.buku book on book.id = trx_detail.buku_id\n" +
                "left join perpustakaan.penerbit pub on pub.id = book.penerbit_id\n" +
                "         left join perpustakaan.anggota agt on agt.id = trx.anggota_id\n" +
                "where trx_detail.transaksi_id = ?\n" +
                "and trx_detail.buku_id = ? \n" +
                "and trx_detail.is_return = false\n" +
                "  ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, transactionId);
        preparedStatement.setString(2, bookId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            preparedStatement.close();
            resultSet.close();
            return Optional.empty();
        }
        TransaksiDetail transaksiDetail = new TransaksiDetail(
                resultSet.getString("id"),
                new Transaksi(
                        resultSet.getString("transactionId"),
                        resultSet.getDate("transactionDate"),
                        new Anggota(
                                resultSet.getString("anggotaId"),
                                resultSet.getString("anggotaKtp"),
                                resultSet.getString("anggotaNama"),
                                resultSet.getString("anggotaAlamat")
                        ),
                        null
                ),
                new Buku(
                        resultSet.getString("buku_id"),
                        resultSet.getString("buku_name"),
                        resultSet.getString("buku_isbn"),
                        new Penerbit(
                                resultSet.getString("penerbit_id"),
                                resultSet.getString("penerbit_nama"),
                                resultSet.getString("penerbit_alamat"),
                                null
                        ),
                        resultSet.getDate("buku_tanggal_terbit")
                ),
                resultSet.getDate("tanggal_kembali"),
                resultSet.getBoolean("status_kembali"),
                resultSet.getDate("last_updated_date")
        );
        preparedStatement.close();
        resultSet.close();
        return Optional.of(transaksiDetail);
    }

    @Override
    public Boolean removeById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "delete\n" +
                "from perpustakaan.transaksi\n" +
                "where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value);
        int row = preparedStatement.executeUpdate();
        return row >= 1;
    }

    @Override
    public Optional<Transaksi> findById(String value) throws SQLException {
        //language=PostgreSQL
        String query = "select \n" +
                "  trx.id as id, \n" +
                "       trx.tanggal_pinjam as tanggal, \n" +
                "       agt.id as anggota_id, \n" +
                "       agt.nomor_ktp as anggota_ktp, \n" +
                "       agt.na" +
                "ma as anggota_nama, \n" +
                "       agt.al" +
                "amat as anggota_alamat\n" +
                "from \n" +
                "perpustakaan.transaksi trx\n" +
                "left join perpustakaan.anggota agt on agt.id = trx.anggota_id \n" +
                "where trx.id = ?\n";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            preparedStatement.close();
            resultSet.close();
            return Optional.empty();
        }
        Transaksi transaksi = new Transaksi(
                resultSet.getString("id"),
                resultSet.getDate("tanggal"),
                new Anggota(
                        resultSet.getString("anggota_id"),
                        resultSet.getString("anggota_ktp"),
                        resultSet.getString("anggota_nama"),
                        resultSet.getString("anggota_alamat")
                ),
                new ArrayList<>()
        );
        return Optional.of(transaksi);
    }

    public List<TransaksiDetail> findByTransactionId(String value) throws SQLException {
        List<TransaksiDetail> list = new ArrayList<>();
        String query = "select trx_detail.id              as id,\n" +
                "       trx_detail.buku_id         as buku_id,\n" +
                "       trx_detail.tanggal_kembali as tanggal_kembali,\n" +
                "       trx_detail.is_return       as status_kembali,\n" +
                "       trx_detail.date_return     as last_updated_date,\n" +
                "       book.id                    as buku_id,\n" +
                "       book.nama                  as buku_name,\n" +
                "       book.isbn                  as buku_isbn,\n" +
                "       book.tanggal_terbit        as buku_tanggal_terbit,\n" +
                "       book.penerbit_id           as penerbit_id,\n" +
                "       pub.nama                   as penerbit_nama,\n" +
                "       pub.alamat                 as penerbit_alamat\n" +
                "from perpustakaan.transaksi_detail trx_detail\n" +
                "         left join perpustakaan.buku book on trx_detail.buku_id = book.id\n" +
                "         left join perpustakaan.penerbit pub on book.penerbit_id = pub.id\n" +
                "where trx_detail.transaksi_id = ?\n";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, value);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            TransaksiDetail transaksiDetail = new TransaksiDetail(
                    resultSet.getString("id"),
                    null,
                    new Buku(
                            resultSet.getString("buku_id"),
                            resultSet.getString("buku_name"),
                            resultSet.getString("buku_isbn"),
                            new Penerbit(
                                    resultSet.getString("penerbit_id"),
                                    resultSet.getString("penerbit_nama"),
                                    resultSet.getString("penerbit_alamat"),
                                    null
                            ),
                            resultSet.getDate("buku_tanggal_terbit")
                    ),
                    resultSet.getDate("tanggal_kembali"),
                    resultSet.getBoolean("status_kembali"),
                    resultSet.getDate("last_updated_date")
            );

            list.add(transaksiDetail);
        }
        preparedStatement.close();
        resultSet.close();
        return list;
    }


    @Override
    public List<Transaksi> findAll() throws SQLException {
        return null;
    }

    @Override
    public List<Transaksi> findAll(Long start, Long limit, String orderDirection, Transaksi param) throws SQLException {
        return null;
    }
}
