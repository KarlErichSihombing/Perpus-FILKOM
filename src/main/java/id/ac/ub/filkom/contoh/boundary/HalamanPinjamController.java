package id.ac.ub.filkom.contoh.boundary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import id.ac.ub.filkom.contoh.control.Bukti;
import id.ac.ub.filkom.contoh.control.PinjamGagalException;
import id.ac.ub.filkom.contoh.control.PinjamService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pinjam")
public class HalamanPinjamController {
    // Tambahkan method privat ini di dalam HalamanPinjamController
    private String idAnggotaLogin(HttpSession session) {
        String id = (String) session.getAttribute("idAnggota");
        if (id == null) {
            // Jika session kosong (misal saat development/testing), kembalikan ID tiruan agar tidak error NullPointer
            return "A001"; 
        }
        return id;
    }

    private final PinjamService pinjamService; // «control»
        public HalamanPinjamController(PinjamService pinjamService) {
    this.pinjamService = pinjamService;
    }
    /** Langkah 1-2 scenario: tampilkan daftar buku beserta stok. */
    @GetMapping
    public String tampilHalaman(HttpSession session, Model model) {
        model.addAttribute("idAnggota", idAnggotaLogin(session));
        model.addAttribute("daftarBuku", pinjamService.daftarBuku());
        return "pinjam"; // templates/pinjam.html
    }
   /** [1] pilihBuku(idBuku) dari aktor Anggota. */
    @PostMapping
    public String pilihBuku(@RequestParam String idBuku, HttpSession session, Model model) {
        String idAnggota = idAnggotaLogin(session);
        try {
            // [2] pinjamBuku(idAnggota, idBuku)
            Bukti bukti = pinjamService.pinjamBuku(idAnggota, idBuku);
            return tampilBukti(bukti, model); // [12] tampilkan bukti
        } catch (PinjamGagalException e) { // [13] pesanHabis
            model.addAttribute("pesan", e.getMessage());
            model.addAttribute("bisaReservasi", e.isBisaReservasi());
            return tampilHalaman(session, model); // [14] pesan & reservasi
        }
    }
    /** +tampilBukti() pada class diagram 8a. */
    private String tampilBukti(Bukti bukti, Model model) {
        model.addAttribute("bukti", bukti);
        return "bukti"; // templates/bukti.html
    } 
}
