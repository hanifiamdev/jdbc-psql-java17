insert into perpustakaan.penerbit(id, nama, alamat)
values ('001', 'Informatika', 'Jakarta'),
       ('002', 'Erlangga', 'Bandung');

insert into perpustakaan.buku(id, nama, isbn, penerbit_id)
values ('001', 'Pemrograman Java', '0001-000-001', '001'),
       ('002', 'ELKS Pelajaran Bahasa Indonesia', '0002-000-002', '002');

insert into perpustakaan.anggota(id, nomor_ktp, nama, alamat)
values ('001', 123, 'Hanif Amrullah', 'Pemalang'),
       ('002', 456, 'Ahmad Orkhan', 'Bandung'),
       ('003', 789, 'Adiba Mujah', 'Jakarta');
