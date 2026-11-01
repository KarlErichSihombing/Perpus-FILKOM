package id.ac.ub.filkom.contoh;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ub.filkom.contoh.control.Bukti;
import id.ac.ub.filkom.contoh.control.PinjamGagalException;
import id.ac.ub.filkom.contoh.control.PinjamService;
import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.TransaksiPinjam;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.TransaksiPinjamRepository;

/** Menguji «control» PinjamService untuk setiap alur pada skenario UC-01 (repository di-mock). */
@ExtendWith(MockitoExtension.class)
class PinjamServiceTest {

    @Mock AnggotaRepository anggotaRepository;
    @Mock BukuRepository bukuRepository;
    @Mock TransaksiPinjamRepository transaksiRepository;
    @InjectMocks PinjamService service;

    @Test
    void alurUtama_stokTersedia_membuatBukti_dan_mengurangiStok() {
        Anggota anggota = new Anggota("A001", "Budi Santoso", 2);
        Buku buku = new Buku("B001", "Pemrograman Java", 3);
        when(anggotaRepository.findById("A001")).thenReturn(Optional.of(anggota));
        when(bukuRepository.findById("B001")).thenReturn(Optional.of(buku));

        Bukti bukti = service.pinjamBuku("A001", "B001");

        assertEquals("Budi Santoso", bukti.getNamaAnggota());
        assertEquals("Pemrograman Java", bukti.getJudulBuku());
        assertEquals(TransaksiPinjam.DIPINJAM, bukti.getStatus());
        assertEquals(2, buku.getStok());
        verify(transaksiRepository).save(any(TransaksiPinjam.class));
        verify(bukuRepository).save(buku);
    }

    @Test
    void alur4a_stokHabis_ditolak_dan_bisaReservasi() {
        Anggota anggota = new Anggota("A001", "Budi Santoso", 2);
        Buku buku = new Buku("B002", "Rekayasa Perangkat Lunak", 0);
        when(anggotaRepository.findById("A001")).thenReturn(Optional.of(anggota));
        when(bukuRepository.findById("B002")).thenReturn(Optional.of(buku));

        PinjamGagalException e = assertThrows(PinjamGagalException.class,
                () -> service.pinjamBuku("A001", "B002"));

        assertTrue(e.isBisaReservasi());
        verify(transaksiRepository, never()).save(any());
    }

    @Test
    void alur4b_melewatiBatas_ditolak_tanpa_reservasi() {
        Anggota anggota = new Anggota("A002", "Sari Dewi", 1);
        Buku buku = new Buku("B003", "Basis Data", 2);
        TransaksiPinjam.buat(anggota, buku);   // sudah meminjam 1 dari batas 1
        when(anggotaRepository.findById("A002")).thenReturn(Optional.of(anggota));
        when(bukuRepository.findById("B003")).thenReturn(Optional.of(buku));

        PinjamGagalException e = assertThrows(PinjamGagalException.class,
                () -> service.pinjamBuku("A002", "B003"));

        assertFalse(e.isBisaReservasi());
        assertEquals(2, buku.getStok());       // stok tidak berubah
        verify(transaksiRepository, never()).save(any());
    }

    @Test
    void anggotaTidakDitemukan_ditolak() {
        when(anggotaRepository.findById("X")).thenReturn(Optional.empty());

        PinjamGagalException e = assertThrows(PinjamGagalException.class,
                () -> service.pinjamBuku("X", "B001"));

        assertEquals("Anggota tidak ditemukan!", e.getMessage());
    }

    @Test
    void bukuTidakDitemukan_ditolak() {
        when(anggotaRepository.findById("A001"))
                .thenReturn(Optional.of(new Anggota("A001", "Budi Santoso", 2)));
        when(bukuRepository.findById("B999")).thenReturn(Optional.empty());

        PinjamGagalException e = assertThrows(PinjamGagalException.class,
                () -> service.pinjamBuku("A001", "B999"));

        assertEquals("Buku tidak ditemukan!", e.getMessage());
    }
}
