alter table perpustakaan.transaksi_detail
    add column is_return boolean default false;

alter table perpustakaan.transaksi_detail
    add column date_return date;