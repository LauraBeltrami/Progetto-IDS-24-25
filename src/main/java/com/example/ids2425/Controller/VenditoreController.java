package com.example.ids2425.Controller;

import com.example.ids2425.Model.Venditore;
import com.example.ids2425.Repository.VenditoreRepository;
import com.example.ids2425.Service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venditori")
public class VenditoreController {

    @Autowired
    private VenditoreRepository venditoreRepo;

    @Autowired
    private EventoService eventoService;

    // ✅ GET: lista di tutti i venditori
    @GetMapping
    public List<Venditore> getAll() {
        return venditoreRepo.findAll();
    }

    // ✅ GET: singolo venditore per id
    @GetMapping("/{id}")
    public ResponseEntity<Venditore> getById(@PathVariable Integer id) {
        return venditoreRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ POST: creazione di un nuovo venditore
    @PostMapping
    public ResponseEntity<Venditore> create(@RequestBody Venditore venditore) {
        return ResponseEntity.ok(venditoreRepo.save(venditore));
    }

    // ✅ PUT: aggiornamento venditore esistente
    @PutMapping("/{id}")
    public ResponseEntity<Venditore> update(
            @PathVariable Integer id,
            @RequestBody Venditore venditoreDetails) {
        return venditoreRepo.findById(id).map(venditore -> {
            venditore.setNome(venditoreDetails.getNome());
            return ResponseEntity.ok(venditoreRepo.save(venditore));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!venditoreRepo.existsById(id)) return ResponseEntity.notFound().build();
        venditoreRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ POST: prenotazione evento per venditore
    @PostMapping("/{id}/prenotaEvento/{eventoId}")
    public ResponseEntity<String> prenotaEvento(
            @PathVariable Integer id,
            @PathVariable Integer eventoId) {
        boolean ok = eventoService.prenotaEventoVenditore(eventoId, id);
        return ok ? ResponseEntity.ok("Venditore prenotato all’evento")
                : ResponseEntity.badRequest().body("Errore nella prenotazione");
    }
}
