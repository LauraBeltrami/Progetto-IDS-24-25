package com.example.ids2425.Controller;

import com.example.ids2425.Model.Acquirente;
import com.example.ids2425.Repository.AcquirenteRepository;
import com.example.ids2425.Service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acquirenti")
public class AcquirenteController {

    @Autowired
    private AcquirenteRepository acquirenteRepo;

    @Autowired
    private EventoService eventoService;

    // ✅ GET: lista di tutti gli acquirenti
    @GetMapping
    public List<Acquirente> getAll() {
        return acquirenteRepo.findAll();
    }

    // ✅ GET: singolo acquirente per id
    @GetMapping("/{id}")
    public ResponseEntity<Acquirente> getById(@PathVariable Integer id) {
        return acquirenteRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ POST: creazione di un nuovo acquirente
    @PostMapping
    public ResponseEntity<Acquirente> create(@RequestBody Acquirente acquirente) {
        return ResponseEntity.ok(acquirenteRepo.save(acquirente));
    }

    // ✅ PUT: aggiornamento acquirente esistente
    @PutMapping("/{id}")
    public ResponseEntity<Acquirente> update(
            @PathVariable Integer id,
            @RequestBody Acquirente acquirenteDetails) {
        return acquirenteRepo.findById(id).map(acquirente -> {
            acquirente.setNome(acquirenteDetails.getNome());
            acquirente.setCognome(acquirenteDetails.getCognome());
            acquirente.setEmail(acquirenteDetails.getEmail());
            return ResponseEntity.ok(acquirenteRepo.save(acquirente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!acquirenteRepo.existsById(id)) return ResponseEntity.notFound().build();
        acquirenteRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    // ✅ POST: prenotazione evento per acquirente
    @PostMapping("/{id}/prenotaEvento/{eventoId}")
    public ResponseEntity<String> prenotaEvento(
            @PathVariable Integer id,
            @PathVariable Integer eventoId) {
        boolean ok = eventoService.prenotaEventoAcquirente(eventoId, id);
        return ok ? ResponseEntity.ok("Acquirente prenotato all’evento")
                : ResponseEntity.badRequest().body("Errore nella prenotazione");
    }
}
