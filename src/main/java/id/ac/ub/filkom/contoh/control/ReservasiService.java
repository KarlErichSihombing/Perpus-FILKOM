package id.ac.ub.filkom.contoh.control;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.Reservasi;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.ReservasiRepository;

/**
 * «control» use case «extend» Ajukan Reservasi (extend dari alur stok habis Pinjam Buku).
 * Di Spring Boot direalisasikan sebagai @Service (BUKAN @Controller).
 */
@Service
public class ReservasiService {

    private final AnggotaRepository anggotaRepository;
    private final BukuRepository bukuRepository;
    private final ReservasiRepository reservasiRepository;

    public ReservasiService(AnggotaRepository anggotaRepository,
                            BukuRepository bukuRepository,
                            ReservasiRepository reservasiRepository) {
        this.anggotaRepository = anggotaRepository;
        this.bukuRepository = bukuRepository;
        this.reservasiRepository = reservasiRepository;
    }

    /** +ajukanReservasi(idAnggota : String, idBuku : String) : BuktiReservasi   — pesan [2]. */
    @Transactional
    public BuktiReservasi ajukanReservasi(String idAnggota, String idBuku) {
        // [3] findById(idAnggota), findById(idBuku) -> [4] anggota, buku
        Anggota anggota = anggotaRepository.findById(idAnggota)
            .orElseThrow(() -> ReservasiGagalException.tidakDitemukan("Anggota"));
        Buku buku = bukuRepository.findById(idBuku)
            .orElseThrow(() -> ReservasiGagalException.tidakDitemukan("Buku"));

        // [5] cekStok() -> [6] stok masih ada? (reservasi hanya untuk buku yang stoknya habis)
        if (buku.cekStok()) {
            throw ReservasiGagalException.stokMasihAda();               // alt [gagal] -> [14]
        }
        // [7] existsByAnggotaAndBukuAndStatus(AKTIF) -> [8] sudah ada?
        if (reservasiRepository.existsByAnggotaAndBukuAndStatus(anggota, buku, Reservasi.AKTIF)) {
            throw ReservasiGagalException.sudahDireservasi();           // alt [gagal] -> [14]
        }
        // alt [stok habis & belum ada reservasi aktif]
        // [9] buat(anggota, buku) -> [10] reservasi
        Reservasi reservasi = Reservasi.buat(anggota, buku);
        // [11] save(reservasi)
        reservasiRepository.save(reservasi);
        return BuktiReservasi.dari(reservasi);                          // [12] bukti reservasi
    }
}
