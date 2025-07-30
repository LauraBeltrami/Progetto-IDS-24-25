package com.example.ids2425.Controller;

import com.example.ids2425.Model.Prodotto;
import com.example.ids2425.Model.Venditore;
import com.example.ids2425.Repository.ProdottoRepository;
import com.example.ids2425.Repository.VenditoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/venditori")
public class VenditoreController {

    private final VenditoreRepository repo;
    private final ProdottoRepository prodottoRepo;

    public VenditoreController(VenditoreRepository repo, ProdottoRepository prodottoRepo) {
        this.repo = repo;
        this.prodottoRepo = prodottoRepo;
    }

    // ---- CRUD venditori ----
    @GetMapping public List<Venditore> list(){ return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Venditore> get(@PathVariable Integer id){
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Venditore create(@RequestBody Venditore v){
        v.setId(null);
        return repo.save(v);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venditore> update(@PathVariable Integer id, @RequestBody Venditore body){
        return repo.findById(id).map(existing -> {
            existing.setNome(body.getNome());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---- PRODOTTI del venditore ----
    public static class ProdottoResponse {
        public Integer id; public String nome; public double prezzo;
        public Integer venditoreId; public String venditoreNome;
        public ProdottoResponse(Prodotto p){
            id=p.getId(); nome=p.getNome(); prezzo=p.getPrezzo();
            if (p.getVenditore()!=null){ venditoreId=p.getVenditore().getId(); venditoreNome=p.getVenditore().getNome(); }
        }
    }

    @GetMapping("/{id}/prodotti")
    public ResponseEntity<List<ProdottoResponse>> prodottiDelVenditore(@PathVariable Integer id){
        if (!repo.existsById(id)) return ResponseEntity.notFound().build(); // 404 se venditore non esiste
        var list = prodottoRepo.findByVenditore_Id(id).stream().map(ProdottoResponse::new).toList();
        return ResponseEntity.ok(list);
    }
}


