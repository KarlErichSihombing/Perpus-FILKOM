package id.ac.ub.filkom.contoh.control;

/** Alur gagal pada use case Pinjam Buku. Ditangkap oleh boundary lalu ditampilkan sebagai pesan. */
public class PinjamGagalException extends RuntimeException {

    private final boolean bisaReservasi;

    public PinjamGagalException(String message) {
        this(message, false);
    }

    public PinjamGagalException(String message, boolean bisaReservasi) {
        super(message);
        this.bisaReservasi = bisaReservasi;
    }

    /** Data tidak ditemukan (validasi input). */
    public static PinjamGagalException tidakDitemukan(String entitas) {
        return new PinjamGagalException(entitas + " tidak ditemukan!");
    }

    /** Alur 4b: melewati batas pinjam. */
    public static PinjamGagalException melewatiBatas() {
        return new PinjamGagalException("Peminjaman gagal! Anggota telah melewati batas maksimal pinjam.");
    }

    /** Alur 4a / pesan [13]: stok habis, pengguna boleh mengajukan reservasi. */
    public static PinjamGagalException stokHabis() {
        return new PinjamGagalException("Peminjaman gagal! Stok buku habis.", true);
    }

    public boolean isBisaReservasi() {
        return bisaReservasi;
    }
}