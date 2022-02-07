package com.hanifiamdev.jdbc.entity.perpustakaan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Buku {

    private String id;
    private String nama;
    private String isbn;
    private Penerbit penerbit;
    private Date tangggalTerbit;
}

