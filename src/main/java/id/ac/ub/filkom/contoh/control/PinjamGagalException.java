package id.ac.ub.filkom.contoh.control;

public class PinjamGagalException extends RuntimeException {
    
    // Constructor utama
    public PinjamGagalException(String message) {
        super(message);
    }

    // [1] Method untuk alur data tidak ditemukan
    public static PinjamGagalException tidakDitemukan(String entitas) {
        return new PinjamGagalException(entitas + " tidak ditemukan!");
    }

    // [2] Method untuk alur melewatiBatas() [Alur 4b]
    public static PinjamGagalException melewatiBatas() {
        return new PinjamGagalException("Peminjaman gagal! Anggota telah melewati batas maksimal pinjam.");
    }

    // [3] Method untuk alur stokHabis() [Alur 13]
    public static PinjamGagalException stokHabis() {
        return new PinjamGagalException("Peminjaman gagal! Stok buku habis.");
    }

    public boolean isBisaReservasi() {
        return this.getMessage() != null && this.getMessage().contains("Stok");
    }
}

