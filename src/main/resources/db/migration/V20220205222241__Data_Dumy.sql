insert into perpustakaan.penerbit(id, nama, alamat)
values ('001', 'Informatika', 'Jakarta'),
       ('002', 'Erlangga', 'Bandung');

insert into perpustakaan.buku(id, nama, isbn, penerbit_id, tanggal_terbit)
values ('001', 'Pemrograman Java', '0001-000-001', '001', to_date('25/12/2021', 'DD/MM/YY')),
       ('002', 'ELKS Pelajaran Bahasa Indonesia', '0002-000-002', '002', to_date('13/02/2022', 'DD/MM/YY')),
       ('003', 'Pemrograman Java 2', '0001-000-001', '001', to_date('24/02/2022', 'DD/MM/YY'));

insert into perpustakaan.anggota(id, nomor_ktp, nama, alamat)
values ('001', 123, 'Hanif Amrullah', 'Pemalang'),
       ('002', 456, 'Ahmad Orkhan', 'Bandung'),
       ('003', 789, 'Adiba Mujah', 'Jakarta');
