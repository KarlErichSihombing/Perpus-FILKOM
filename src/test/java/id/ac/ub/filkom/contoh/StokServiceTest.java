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

import id.ac.ub.filkom.contoh.control.StokGagalException;
import id.ac.ub.filkom.contoh.control.StokService;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.repository.BukuRepository;

/** Menguji «control» StokService untuk setiap alur pada use case Kelola Stok Buku (repository di-mock). */
@ExtendWith(MockitoExtension.class)
class StokServiceTest {

    @Mock BukuRepository bukuRepository;
    @InjectMocks StokService service;

    @Test
    void alurUtama_tambahStok_menambah_dan_menyimpan() {
        Buku buku = new Buku("B002", "Rekayasa Perangkat Lunak", 0);
        when(bukuRepository.findById("B002")).thenReturn(Optional.of(buku));

        Buku hasil = service.tambahStok("B002", 4);

        assertEquals(4, hasil.getStok());
        verify(bukuRepository).save(buku);
    }

    @Test
    void bukuTidakDitemukan_ditolak() {
        when(bukuRepository.findById("B999")).thenReturn(Optional.empty());

        StokGagalException e = assertThrows(StokGagalException.class,
                () -> service.tambahStok("B999", 1));

        assertEquals("Buku tidak ditemukan!", e.getMessage());
        verify(bukuRepository, never()).save(any());
    }

    @Test
    void idBukuKosong_ditolak_tanpa_menyentuh_repository() {
        assertThrows(StokGagalException.class, () -> service.tambahStok("  ", 1));
        verify(bukuRepository, never()).findById(any());
    }

    @Test
    void jumlahTidakValid_ditolak_dan_stok_tidak_berubah() {
        Buku buku = new Buku("B001", "Pemrograman Java", 3);
        when(bukuRepository.findById("B001")).thenReturn(Optional.of(buku));

        assertThrows(StokGagalException.class, () -> service.tambahStok("B001", 0));

        assertEquals(3, buku.getStok());
        verify(bukuRepository, never()).save(any());
    }
}
