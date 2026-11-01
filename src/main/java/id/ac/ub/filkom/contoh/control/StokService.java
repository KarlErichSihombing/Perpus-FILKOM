package id.ac.ub.filkom.contoh.control;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.repository.BukuRepository;

/**
 * «control» use case Kelola Stok Buku (aktor Pustakawan).
 * Di Spring Boot direalisasikan sebagai @Service (BUKAN @Controller):
 * tugasnya mengoordinasikan urutan pesan pada sequence diagram.
 */
@Service
public class StokService {

    private final BukuRepository bukuRepository;

    public StokService(BukuRepository bukuRepository) {
        this.bukuRepository = bukuRepository;
    }

    /** Daftar buku beserta stok untuk ditampilkan di halaman stok. */
    public List<Buku> daftarBuku() {
        return bukuRepository.findAll();
    }

    /** +tambahStok(idBuku : String, jumlah : int) : Buku   — pesan [2]. */
    @Transactional
    public Buku tambahStok(String idBuku, int jumlah) {
        if (idBuku == null || idBuku.isBlank()) {
            throw StokGagalException.tidakDitemukan();
        }
        // [3] findById(idBuku) -> [4] buku
        Buku buku = bukuRepository.findById(idBuku.trim())
            .orElseThrow(StokGagalException::tidakDitemukan);   // alt [gagal] -> [10]

        // alt [buku ditemukan & jumlah valid]
        // [5] tambahStok(jumlah) -> [6] ok
        try {
            buku.tambahStok(jumlah);
        } catch (IllegalArgumentException e) {
            throw StokGagalException.jumlahTidakValid(e.getMessage()); // alt [gagal] -> [10]
        }
        // [7] save(buku)
        bukuRepository.save(buku);
        return buku;                                             // [8] stokBaru
    }
}
