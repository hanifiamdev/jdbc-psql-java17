package com.hanifiamdev.jdbc.entity.perpustakaan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"details"})
public class Transaksi {

    private String id;
    private Date createedDate;
    private Anggota anggota;
    private List<TransaksiDetail> details = new ArrayList<>();
}
