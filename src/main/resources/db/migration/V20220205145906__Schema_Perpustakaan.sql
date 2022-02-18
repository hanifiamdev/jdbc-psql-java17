create schema if not exists perpustakaan;

create table perpustakaan.buku
(
    id             character varying(64) not null primary key default gen_random_uuid(),
    nama           character varying(64) not null,
    isbn           character varying(50) not null,
    penerbit_id    character varying(64),
    tanggal_terbit date
);

create table perpustakaan.penerbit
(
    id     character varying(64) not null primary key default gen_random_uuid(),
    nama   character varying(50) not null,
    alamat text
);

alter table perpustakaan.buku
    add constraint fk_penerbit_id foreign key (penerbit_id)
        references perpustakaan.penerbit (id) on update set null on delete set default;

create table perpustakaan.anggota
(
    id        character varying(64) not null primary key default gen_random_uuid(),
    nomor_ktp int                   not null unique,
    nama      character varying(50) not null,
    alamat    text
);

create table perpustakaan.transaksi
(
    id             character varying(64) not null primary key default gen_random_uuid(),
    tanggal_pinjam date                  not null             default now(),
    anggota_id     character varying(64) not null
);

alter table perpustakaan.transaksi
    add constraint fk_transaksi_anggota foreign key (anggota_id)
        references perpustakaan.anggota (id) on update cascade on delete cascade;

create table perpustakaan.transaksi_detail
(
    id              character varying(64) not null primary key default gen_random_uuid(),
    transaksi_id    character varying(50) not null,
    buku_id         character varying(64) not null,
    tanggal_kembali date                  not null
);

alter table perpustakaan.transaksi_detail
    add constraint fk_buku_id foreign key (buku_id)
        references perpustakaan.buku (id) on update cascade on delete cascade;

alter table perpustakaan.transaksi_detail
    add constraint fk_transaksi_id foreign key (transaksi_id)
        references perpustakaan.transaksi (id) on update cascade on delete cascade;





