package com.hanifiamdev.jdbc.entity.perpustakaan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anggota {

    private String id;
    private String nomorKtp;
    private String nama;
    private String alamat;

}
