package id.ac.ub.filkom.contoh.boundary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import id.ac.ub.filkom.contoh.control.StokGagalException;
import id.ac.ub.filkom.contoh.control.StokService;
import id.ac.ub.filkom.contoh.entity.Buku;

/** «boundary» HalamanStok (bagian penerima request) — aktor Pustakawan. */
@Controller
@RequestMapping("/stok")
public class HalamanStokController {

    private final StokService stokService; // «control»

    public HalamanStokController(StokService stokService) {
        this.stokService = stokService;
    }

    /** Prasyarat scenario: tampilkan daftar buku beserta stok. */
    @GetMapping
    public String tampilHalaman(Model model) {
        model.addAttribute("daftarBuku", stokService.daftarBuku());
        return "stok"; // templates/stok.html
    }

    /** [1] tambahStok(idBuku, jumlah) dari aktor Pustakawan. */
    @PostMapping("/tambah")
    public String tambahStok(@RequestParam String idBuku,
                             @RequestParam int jumlah,
                             RedirectAttributes redirect,
                             Model model) {
        try {
            // [2] tambahStok(idBuku, jumlah)
            Buku buku = stokService.tambahStok(idBuku, jumlah);
            // [8] stokBaru -> [9] tampilkan daftar stok terbaru + pesan sukses (pola Post-Redirect-Get)
            redirect.addFlashAttribute("sukses",
                "Stok \"" + buku.getJudul() + "\" bertambah menjadi " + buku.getStok() + ".");
            return "redirect:/stok";
        } catch (StokGagalException e) { // [10] throw StokGagalException
            model.addAttribute("pesan", e.getMessage());
            return tampilHalaman(model); // [11] tampilkan pesan gagal
        }
    }
}
