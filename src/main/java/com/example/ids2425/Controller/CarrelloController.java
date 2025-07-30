package com.example.ids2425.Controller;

import com.example.ids2425.Model.*;
import com.example.ids2425.Repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarrelloController {

    private final CarrelloRepository carrelloRepo;
    private final AcquirenteRepository acqRepo;
    private final ProdottoRepository prodRepo;

    public CarrelloController(CarrelloRepository carrelloRepo,
                              AcquirenteRepository acqRepo,
                              ProdottoRepository prodRepo) {
        this.carrelloRepo = carrelloRepo;
        this.acqRepo = acqRepo;
        this.prodRepo = prodRepo;
    }

    // ----- DTO -----
    public static class AddItemRequest {
        public Integer prodottoId;
        public int quantita;
    }

    public static class ItemResponse {
        public Integer prodottoId;
        public String nome;
        public double prezzo;
        public int quantita;
        public double subtotale;

        public ItemResponse(CarrelloItem ci) {
            this.prodottoId = ci.getProdotto().getId();
            this.nome = ci.getProdotto().getNome();
            this.prezzo = ci.getProdotto().getPrezzo();
            this.quantita = ci.getQuantita();
            this.subtotale = ci.getTotaleRiga();
        }
    }

    public static class CarrelloResponse {
        public Integer id;
        public Integer acquirenteId;
        public int numeroArticoli;
        public double totale;
        public List<ItemResponse> items;

        public CarrelloResponse(Carrello c) {
            this.id = c.getId();
            this.acquirenteId = c.getAcquirente() != null ? c.getAcquirente().getId() : null;
            this.numeroArticoli = c.getNumeroArticoli();
            this.totale = c.getTotale();
            this.items = c.getItems().stream().map(ItemResponse::new).toList();
        }
    }
    // ----------------

    /** Crea (se non esiste) il carrello per l’acquirente e lo ritorna con gli items. */
    @Transactional
    @PostMapping("/api/acquirenti/{acquirenteId}/carrello")
    public ResponseEntity<CarrelloResponse> creaCarrello(@PathVariable Integer acquirenteId) {
        Acquirente acq = acqRepo.findById(acquirenteId).orElse(null);
        if (acq == null) return ResponseEntity.notFound().build();

        Carrello existing = carrelloRepo.findWithItemsByAcquirente_Id(acquirenteId).orElse(null);
        if (existing != null) return ResponseEntity.ok(new CarrelloResponse(existing));

        Carrello nuovo = new Carrello();
        nuovo.setAcquirente(acq);
        carrelloRepo.save(nuovo);

        Carrello saved = carrelloRepo.findWithItemsByAcquirente_Id(acquirenteId).orElse(nuovo);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CarrelloResponse(saved));
    }

    /** Aggiunge (o incrementa) un item nel carrello. */
    @Transactional
    @PostMapping("/api/carrelli/{carrelloId}/items")
    public ResponseEntity<CarrelloResponse> aggiungiItem(@PathVariable Integer carrelloId,
                                                         @RequestBody AddItemRequest body) {
        Carrello carrello = carrelloRepo.findWithItemsById(carrelloId).orElse(null);
        if (carrello == null) return ResponseEntity.notFound().build();

        Prodotto prodotto = prodRepo.findById(body.prodottoId).orElse(null);
        if (prodotto == null || body.quantita <= 0) return ResponseEntity.badRequest().build();

        CarrelloItem existing = carrello.getItems().stream()
                .filter(ci -> ci.getProdotto().getId().equals(prodotto.getId()))
                .findFirst().orElse(null);

        if (existing != null) {
            existing.setQuantita(existing.getQuantita() + body.quantita);
        } else {
            carrello.getItems().add(new CarrelloItem(null, carrello, prodotto, body.quantita));
        }

        carrelloRepo.save(carrello);
        Carrello saved = carrelloRepo.findWithItemsById(carrelloId).orElse(carrello);
        return ResponseEntity.ok(new CarrelloResponse(saved));
    }

    /** Rimuove completamente l’item di quel prodotto. */
    @Transactional
    @DeleteMapping("/api/carrelli/{carrelloId}/items/{prodottoId}")
    public ResponseEntity<CarrelloResponse> rimuoviItem(@PathVariable Integer carrelloId,
                                                        @PathVariable Integer prodottoId) {
        Carrello carrello = carrelloRepo.findWithItemsById(carrelloId).orElse(null);
        if (carrello == null) return ResponseEntity.notFound().build();

        carrello.getItems().removeIf(ci -> ci.getProdotto().getId().equals(prodottoId));
        carrelloRepo.save(carrello);

        Carrello saved = carrelloRepo.findWithItemsById(carrelloId).orElse(carrello);
        return ResponseEntity.ok(new CarrelloResponse(saved));
    }

    /** Dettaglio carrello con items e totale. */
    @Transactional(readOnly = true)
    @GetMapping("/api/carrelli/{carrelloId}")
    public ResponseEntity<CarrelloResponse> getCarrello(@PathVariable Integer carrelloId) {
        return carrelloRepo.findWithItemsById(carrelloId)
                .map(c -> ResponseEntity.ok(new CarrelloResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }
}
