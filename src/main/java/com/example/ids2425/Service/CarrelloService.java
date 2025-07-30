package com.example.ids2425.Service;

import com.example.ids2425.Model.*;
import com.example.ids2425.Repository.CarrelloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarrelloService {

    private final CarrelloRepository carrelloRepo;

    public CarrelloService(CarrelloRepository carrelloRepo) {
        this.carrelloRepo = carrelloRepo;
    }

    @Transactional
    public Carrello aggiungiProdotto(Carrello carrello, Prodotto prodotto, int quantita) {
        if (carrello == null || prodotto == null || quantita <= 0) return carrello;

        var existing = carrello.getItems().stream()
                .filter(ci -> ci.getProdotto().getId().equals(prodotto.getId()))
                .findFirst().orElse(null);

        if (existing != null) {
            existing.setQuantita(existing.getQuantita() + quantita);
        } else {
            carrello.getItems().add(new CarrelloItem(null, carrello, prodotto, quantita));
        }
        return carrelloRepo.save(carrello);
    }

    @Transactional
    public Carrello rimuoviProdotto(Carrello carrello, Prodotto prodotto) {
        if (carrello == null || prodotto == null) return carrello;
        carrello.getItems().removeIf(ci -> ci.getProdotto().getId().equals(prodotto.getId()));
        return carrelloRepo.save(carrello);
    }

    /** Totale corrente del carrello */
    public double totale(Carrello carrello) {
        return (carrello == null) ? 0.0 : carrello.getTotale();
    }

    @Transactional
    public Carrello svuota(Carrello carrello) {
        if (carrello == null) return null;
        carrello.getItems().clear();
        return carrelloRepo.save(carrello);
    }

    /** Esempio di checkout “in memoria” (Pagamento non JPA): compila, non è usato dai controller */
    public Pagamento checkout(Carrello carrello, String metodo) {
        if (carrello == null || carrello.getNumeroArticoli() == 0) {
            throw new IllegalStateException("Carrello vuoto, impossibile fare il checkout.");
        }
        double tot = carrello.getTotale();
        Pagamento pagamento = new Pagamento(
                1, // id fittizio
                carrello.getAcquirente(),
                tot,
                metodo
        );
        pagamento.completa();
        carrello.getItems().clear();
        carrelloRepo.save(carrello);
        return pagamento;
    }
}
