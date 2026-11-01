package id.ac.ub.filkom.contoh.control;

import id.ac.ub.filkom.contoh.entity.TransaksiPinjam;

public class Bukti {
    private final String idTransaksi;
    private final String namaAnggota;
    private final String judulBuku;

    // Constructor untuk membuat objek Bukti
    public Bukti(String idTransaksi, String namaAnggota, String judulBuku) {
        this.idTransaksi = idTransaksi;
        this.namaAnggota = namaAnggota;
        this.judulBuku = judulBuku;
    }

    /** 
     * Static method yang dipanggil di PinjamService.
     * Mengubah data dari TransaksiPinjam menjadi format Bukti.
     */
    public static Bukti dari(TransaksiPinjam transaksi) {
        return new Bukti(
            transaksi.getIdTransaksi(), // Pastikan nama getter ini sama di class TransaksiPinjam
            transaksi.getAnggota().getNama(),
            transaksi.getBuku().getJudul() // Pastikan nama getter ini sama di class Buku
        );
    }

    // Getter agar nilainya bisa dibaca oleh Controller atau View (HTML)
    public String getIdTransaksi() { 
        return idTransaksi; 
    }

    public String getNamaAnggota() { 
        return namaAnggota; 
    }

    public String getJudulBuku() { 
        return judulBuku; 
    }
}
