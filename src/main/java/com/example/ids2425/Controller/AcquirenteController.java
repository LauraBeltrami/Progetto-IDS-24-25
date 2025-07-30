package com.example.ids2425.Controller;

import com.example.ids2425.Model.Acquirente;
import com.example.ids2425.Repository.AcquirenteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acquirenti")
public class AcquirenteController {

    private final AcquirenteRepository repo;
    public AcquirenteController(AcquirenteRepository repo) { this.repo = repo; }

    @GetMapping public List<Acquirente> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Acquirente> get(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Acquirente create(@RequestBody Acquirente a) {
        a.setId(null); // id generato dal DB
        return repo.save(a);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Acquirente> update(@PathVariable Integer id, @RequestBody Acquirente body) {
        return repo.findById(id).map(existing -> {
            existing.setNome(body.getNome());
            existing.setCognome(body.getCognome());
            existing.setEmail(body.getEmail());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}