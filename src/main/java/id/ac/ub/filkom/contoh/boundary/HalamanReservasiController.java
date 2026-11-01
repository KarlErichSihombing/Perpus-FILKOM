package id.ac.ub.filkom.contoh.boundary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import id.ac.ub.filkom.contoh.control.BuktiReservasi;
import id.ac.ub.filkom.contoh.control.ReservasiGagalException;
import id.ac.ub.filkom.contoh.control.ReservasiService;
import jakarta.servlet.http.HttpSession;

/** «boundary» HalamanReservasi (bagian penerima request) — aktor Anggota. */
@Controller
@RequestMapping("/reservasi")
public class HalamanReservasiController {

    private final ReservasiService reservasiService; // «control»

    public HalamanReservasiController(ReservasiService reservasiService) {
        this.reservasiService = reservasiService;
    }

    /** Sama dengan HalamanPinjamController: fallback ID tiruan saat session kosong (development). */
    private String idAnggotaLogin(HttpSession session) {
        String id = (String) session.getAttribute("idAnggota");
        return (id == null) ? "A001" : id;
    }

    /** [1] ajukanReservasi(idBuku) dari aktor Anggota (tautan pada pinjam.html). */
    @PostMapping
    public String ajukanReservasi(@RequestParam String idBuku, HttpSession session, Model model) {
        String idAnggota = idAnggotaLogin(session);
        try {
            // [2] ajukanReservasi(idAnggota, idBuku)
            BuktiReservasi bukti = reservasiService.ajukanReservasi(idAnggota, idBuku);
            model.addAttribute("bukti", bukti); // [13] tampilkan bukti reservasi
        } catch (ReservasiGagalException e) {   // [14] throw ReservasiGagalException
            model.addAttribute("pesan", e.getMessage()); // [15] tampilkan pesan gagal
        }
        return "reservasi"; // templates/reservasi.html
    }
}
