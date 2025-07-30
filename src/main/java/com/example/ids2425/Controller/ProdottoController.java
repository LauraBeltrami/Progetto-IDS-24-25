package com.example.ids2425.Controller;

import com.example.ids2425.Model.Prodotto;
import com.example.ids2425.Model.Venditore;
import com.example.ids2425.Repository.ProdottoRepository;
import com.example.ids2425.Repository.VenditoreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {

    private final ProdottoRepository repo;
    private final VenditoreRepository vendRepo;

    public ProdottoController(ProdottoRepository repo, VenditoreRepository vendRepo) {
        this.repo = repo;
        this.vendRepo = vendRepo;
    }

    // ---- DTO minimi per request/response ----
    public static class ProdottoRequest {
        public String nome;
        public double prezzo;
        public Integer venditoreId; // opzionale
    }

    public static class ProdottoResponse {
        public Integer id;
        public String nome;
        public double prezzo;
        public Integer venditoreId;
        public String venditoreNome;
        public ProdottoResponse(Prodotto p) {
            this.id = p.getId();
            this.nome = p.getNome();
            this.prezzo = p.getPrezzo();
            if (p.getVenditore() != null) {
                this.venditoreId = p.getVenditore().getId();
                this.venditoreNome = p.getVenditore().getNome();
            }
        }
    }
    private ProdottoResponse toResp(Prodotto p) { return new ProdottoResponse(p); }
    // -----------------------------------------

    @GetMapping
    public List<ProdottoResponse> list() {
        return repo.findAll().stream().map(this::toResp).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdottoResponse> get(@PathVariable Integer id) {
        return repo.findById(id)
                .map(p -> ResponseEntity.ok(toResp(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProdottoResponse> create(@RequestBody ProdottoRequest body) {
        // recupera (se passato) il venditore
        Venditore vend = null;
        if (body.venditoreId != null) {
            vend = vendRepo.findById(body.venditoreId).orElse(null);
        }
        Prodotto p = new Prodotto();
        p.setNome(body.nome);
        p.setPrezzo(body.prezzo);
        p.setVenditore(vend);
        Prodotto saved = repo.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResp(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdottoResponse> update(@PathVariable Integer id, @RequestBody ProdottoRequest body) {
        return repo.findById(id).map(existing -> {
            existing.setNome(body.nome);
            existing.setPrezzo(body.prezzo);
            if (body.venditoreId != null) {
                Venditore vend = vendRepo.findById(body.venditoreId).orElse(null);
                existing.setVenditore(vend);
            }
            return ResponseEntity.ok(toResp(repo.save(existing)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}