package com.hanifiamdev.jdbc.entity.perpustakaan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaksiDetail {

    private String id;
    private Transaksi transaksi;
    private Buku buku;
    private Date tanggalKembali;
    private Boolean statusKembali;
    private Date lastUpdateDate;

}
