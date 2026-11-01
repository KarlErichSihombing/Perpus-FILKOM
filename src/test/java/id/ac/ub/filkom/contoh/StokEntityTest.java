package id.ac.ub.filkom.contoh;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.Reservasi;

/** Unit test method entity baru: Buku.tambahStok() dan Reservasi (tanpa database/server). */
class StokEntityTest {

    @Test
    void tambahStok_menambah_stok_dan_membuat_buku_tersedia_lagi() {
        Buku buku = new Buku("B002", "Rekayasa Perangkat Lunak", 0);
        assertFalse(buku.cekStok());

        buku.tambahStok(5);

        assertEquals(5, buku.getStok());
        assertTrue(buku.cekStok());
    }

    @Test
    void tambahStok_menolak_jumlah_nol_atau_negatif() {
        Buku buku = new Buku("B001", "Pemrograman Java", 3);

        assertThrows(IllegalArgumentException.class, () -> buku.tambahStok(0));
        assertThrows(IllegalArgumentException.class, () -> buku.tambahStok(-2));
        assertEquals(3, buku.getStok());   // stok tidak berubah
    }

    @Test
    void reservasi_buat_mengisi_data_dan_berstatus_aktif() {
        Anggota anggota = new Anggota("A001", "Budi Santoso", 2);
        Buku buku = new Buku("B002", "Rekayasa Perangkat Lunak", 0);

        Reservasi r = Reservasi.buat(anggota, buku);

        assertNotNull(r.getIdReservasi());
        assertEquals(9, r.getIdReservasi().length());
        assertEquals(anggota, r.getAnggota());
        assertEquals(buku, r.getBuku());
        assertEquals(Reservasi.AKTIF, r.getStatus());
        assertTrue(r.isAktif());
        assertNotNull(r.getTanggalReservasi());
    }

    @Test
    void reservasi_buat_menolak_anggota_atau_buku_null() {
        Anggota anggota = new Anggota("A001", "Budi Santoso", 2);
        Buku buku = new Buku("B002", "Rekayasa Perangkat Lunak", 0);

        assertThrows(NullPointerException.class, () -> Reservasi.buat(null, buku));
        assertThrows(NullPointerException.class, () -> Reservasi.buat(anggota, null));
    }
}
