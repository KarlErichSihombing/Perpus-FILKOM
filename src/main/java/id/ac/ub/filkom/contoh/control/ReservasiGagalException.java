package id.ac.ub.filkom.contoh.control;

/** Alur gagal pada use case Ajukan Reservasi. Ditangkap oleh boundary lalu ditampilkan sebagai pesan. */
public class ReservasiGagalException extends RuntimeException {

    public ReservasiGagalException(String message) {
        super(message);
    }

    /** Data tidak ditemukan (validasi input). */
    public static ReservasiGagalException tidakDitemukan(String entitas) {
        return new ReservasiGagalException(entitas + " tidak ditemukan!");
    }

    /** Stok masih ada, jadi tidak perlu reservasi: arahkan pengguna untuk meminjam. */
    public static ReservasiGagalException stokMasihAda() {
        return new ReservasiGagalException("Reservasi tidak diperlukan! Buku masih tersedia, silakan pinjam langsung.");
    }

    /** Anggota sudah punya reservasi aktif untuk buku yang sama. */
    public static ReservasiGagalException sudahDireservasi() {
        return new ReservasiGagalException("Reservasi gagal! Kamu sudah mengajukan reservasi untuk buku ini.");
    }
}
