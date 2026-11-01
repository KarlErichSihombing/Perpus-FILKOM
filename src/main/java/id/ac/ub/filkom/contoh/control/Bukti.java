package id.ac.ub.filkom.contoh.control;

import java.time.LocalDate;

import id.ac.ub.filkom.contoh.entity.TransaksiPinjam;

/** Data hasil «control» yang dikembalikan ke boundary (pesan [11] bukti peminjaman). */
public class Bukti {
    private final String idTransaksi;
    private final String namaAnggota;
    private final String judulBuku;
    private final LocalDate tanggalPinjam;
    private final String status;

    public Bukti(String idTransaksi, String namaAnggota, String judulBuku,
                 LocalDate tanggalPinjam, String status) {
        this.idTransaksi = idTransaksi;
        this.namaAnggota = namaAnggota;
        this.judulBuku = judulBuku;
        this.tanggalPinjam = tanggalPinjam;
        this.status = status;
    }

    /** Mengubah TransaksiPinjam menjadi Bukti (dipanggil di PinjamService). */
    public static Bukti dari(TransaksiPinjam transaksi) {
        return new Bukti(
            transaksi.getIdTransaksi(),
            transaksi.getAnggota().getNama(),
            transaksi.getBuku().getJudul(),
            transaksi.getTanggalPinjam(),
            transaksi.getStatus());
    }

    public String getIdTransaksi()      { return idTransaksi; }
    public String getNamaAnggota()      { return namaAnggota; }
    public String getJudulBuku()        { return judulBuku; }
    public LocalDate getTanggalPinjam() { return tanggalPinjam; }
    public String getStatus()           { return status; }
}