package id.ac.ub.filkom.contoh.repository;

import org.springframework.data.jpa.repository.JpaRepository; // 🌟 Tambahkan import ini
import id.ac.ub.filkom.contoh.entity.TransaksiPinjam;

/**
 * Pastikan interface ini extends JpaRepository agar method .save() otomatis tersedia.
 * Ganti 'String' dengan tipe data ID dari TransaksiPinjam jika tipe datanya bukan String (misal Long atau Integer).
 */
public interface TransaksiPinjamRepository extends JpaRepository<TransaksiPinjam, String> {
    // Kosongkan tidak apa-apa, method save() sudah otomatis diwarisi dari JpaRepository
}
