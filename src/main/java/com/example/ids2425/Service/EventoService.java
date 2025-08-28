package com.example.ids2425.Service;

import com.example.ids2425.Model.Evento;
import com.example.ids2425.Model.Prodotto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoService {

    private List<Evento> eventi = new ArrayList<>();

    public Evento creaEvento(int id, String nome, String luogo, LocalDateTime data, String descrizione) {
        Evento e = new Evento(id, nome, luogo, data, descrizione);
        eventi.add(e);
        return e;
    }

    public void aggiungiProdotto(Evento e, Prodotto p) {
        if (e == null || p == null) return;
        e.aggiungiProdotto(p);
    }

    public void rimuoviProdotto(Evento e, Prodotto p) {
        if (e == null || p == null) return;
        e.rimuoviProdotto(p);
    }

    // 🔹 Nuovo metodo: elimina evento
    public boolean eliminaEvento(int id) {
        return eventi.removeIf(e -> e.getId() == id);
    }

    // Opzionale: per ottenere la lista completa
    public List<Evento> getEventi() {
        return eventi;
    }
}
