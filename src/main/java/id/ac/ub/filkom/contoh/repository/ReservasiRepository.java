package id.ac.ub.filkom.contoh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.Reservasi;

/** Akses tabel reservasi (implementasi otomatis Spring Data JPA). */
public interface ReservasiRepository extends JpaRepository<Reservasi, String> {

    /** Dipakai control untuk mencegah reservasi ganda (anggota + buku + status). */
    boolean existsByAnggotaAndBukuAndStatus(Anggota anggota, Buku buku, String status);
}
