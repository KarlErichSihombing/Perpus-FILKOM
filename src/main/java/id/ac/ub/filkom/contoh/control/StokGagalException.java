package id.ac.ub.filkom.contoh.control;

/** Alur gagal pada use case Kelola Stok Buku. Ditangkap oleh boundary lalu ditampilkan sebagai pesan. */
public class StokGagalException extends RuntimeException {

    public StokGagalException(String message) {
        super(message);
    }

    /** ID buku kosong atau tidak terdaftar (validasi input). */
    public static StokGagalException tidakDitemukan() {
        return new StokGagalException("Buku tidak ditemukan!");
    }

    /** Jumlah tambahan stok tidak valid (kurang dari atau sama dengan 0). */
    public static StokGagalException jumlahTidakValid(String alasan) {
        return new StokGagalException("Stok gagal ditambah! " + alasan + ".");
    }
}
