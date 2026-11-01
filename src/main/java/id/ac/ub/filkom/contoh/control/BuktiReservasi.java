package id.ac.ub.filkom.contoh.control;

import java.time.LocalDate;

import id.ac.ub.filkom.contoh.entity.Reservasi;

/** Data hasil «control» yang dikembalikan ke boundary (pesan [12] bukti reservasi). */
public class BuktiReservasi {
    private final String idReservasi;
    private final String namaAnggota;
    private final String judulBuku;
    private final LocalDate tanggalReservasi;
    private final String status;

    public BuktiReservasi(String idReservasi, String namaAnggota, String judulBuku,
                          LocalDate tanggalReservasi, String status) {
        this.idReservasi = idReservasi;
        this.namaAnggota = namaAnggota;
        this.judulBuku = judulBuku;
        this.tanggalReservasi = tanggalReservasi;
        this.status = status;
    }

    /** Mengubah Reservasi menjadi BuktiReservasi (dipanggil di ReservasiService). */
    public static BuktiReservasi dari(Reservasi reservasi) {
        return new BuktiReservasi(
            reservasi.getIdReservasi(),
            reservasi.getAnggota().getNama(),
            reservasi.getBuku().getJudul(),
            reservasi.getTanggalReservasi(),
            reservasi.getStatus());
    }

    public String getIdReservasi()          { return idReservasi; }
    public String getNamaAnggota()          { return namaAnggota; }
    public String getJudulBuku()            { return judulBuku; }
    public LocalDate getTanggalReservasi()  { return tanggalReservasi; }
    public String getStatus()               { return status; }
}
