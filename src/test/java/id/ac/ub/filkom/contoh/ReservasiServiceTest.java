package id.ac.ub.filkom.contoh;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import id.ac.ub.filkom.contoh.control.BuktiReservasi;
import id.ac.ub.filkom.contoh.control.ReservasiGagalException;
import id.ac.ub.filkom.contoh.control.ReservasiService;
import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.Reservasi;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.ReservasiRepository;

/** Menguji «control» ReservasiService untuk setiap alur pada use case Ajukan Reservasi (repository di-mock). */
@ExtendWith(MockitoExtension.class)
class ReservasiServiceTest {

    @Mock AnggotaRepository anggotaRepository;
    @Mock BukuRepository bukuRepository;
    @Mock ReservasiRepository reservasiRepository;
    @InjectMocks ReservasiService service;

    @Test
    void alurUtama_stokHabis_membuat_dan_menyimpan_reservasi() {
        Anggota anggota = new Anggota("A001", "Budi Santoso", 2);
        Buku buku = new Buku("B002", "Rekayasa Perangkat Lunak", 0);
        when(anggotaRepository.findById("A001")).thenReturn(Optional.of(anggota));
        when(bukuRepository.findById("B002")).thenReturn(Optional.of(buku));
        when(reservasiRepository.existsByAnggotaAndBukuAndStatus(anggota, buku, Reservasi.AKTIF))
                .thenReturn(false);

        BuktiReservasi bukti = service.ajukanReservasi("A001", "B002");

        assertEquals("Budi Santoso", bukti.getNamaAnggota());
        assertEquals("Rekayasa Perangkat Lunak", bukti.getJudulBuku());
        assertEquals(Reservasi.AKTIF, bukti.getStatus());
        verify(reservasiRepository).save(any(Reservasi.class));
    }

    @Test
    void stokMasihAda_ditolak_karena_reservasi_tidak_diperlukan() {
        Anggota anggota = new Anggota("A001", "Budi Santoso", 2);
        Buku buku = new Buku("B001", "Pemrograman Java", 3);
        when(anggotaRepository.findById("A001")).thenReturn(Optional.of(anggota));
        when(bukuRepository.findById("B001")).thenReturn(Optional.of(buku));

        assertThrows(ReservasiGagalException.class, () -> service.ajukanReservasi("A001", "B001"));

        verify(reservasiRepository, never()).save(any());
    }

    @Test
    void sudahDireservasi_ditolak_tanpa_reservasi_ganda() {
        Anggota anggota = new Anggota("A001", "Budi Santoso", 2);
        Buku buku = new Buku("B002", "Rekayasa Perangkat Lunak", 0);
        when(anggotaRepository.findById("A001")).thenReturn(Optional.of(anggota));
        when(bukuRepository.findById("B002")).thenReturn(Optional.of(buku));
        when(reservasiRepository.existsByAnggotaAndBukuAndStatus(anggota, buku, Reservasi.AKTIF))
                .thenReturn(true);

        assertThrows(ReservasiGagalException.class, () -> service.ajukanReservasi("A001", "B002"));

        verify(reservasiRepository, never()).save(any());
    }

    @Test
    void anggotaTidakDitemukan_ditolak() {
        when(anggotaRepository.findById("X")).thenReturn(Optional.empty());

        ReservasiGagalException e = assertThrows(ReservasiGagalException.class,
                () -> service.ajukanReservasi("X", "B002"));

        assertEquals("Anggota tidak ditemukan!", e.getMessage());
    }

    @Test
    void bukuTidakDitemukan_ditolak() {
        when(anggotaRepository.findById("A001"))
                .thenReturn(Optional.of(new Anggota("A001", "Budi Santoso", 2)));
        when(bukuRepository.findById("B999")).thenReturn(Optional.empty());

        ReservasiGagalException e = assertThrows(ReservasiGagalException.class,
                () -> service.ajukanReservasi("A001", "B999"));

        assertEquals("Buku tidak ditemukan!", e.getMessage());
    }
}
