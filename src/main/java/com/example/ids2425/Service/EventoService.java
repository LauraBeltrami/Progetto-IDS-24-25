package com.example.ids2425.Service;

import com.example.ids2425.Model.Evento;
import com.example.ids2425.Model.Prodotto;

import java.time.LocalDateTime;

public class EventoService {

    public Evento creaEvento(int id, String nome, String luogo, LocalDateTime data, String descrizione) {
        return new Evento(id, nome, luogo, data, descrizione);
    }

    public void aggiungiProdotto(Evento e, Prodotto p) {
        if (e == null || p == null) return;
        e.aggiungiProdotto(p);
    }

    public void rimuoviProdotto(Evento e, Prodotto p) {
        if (e == null || p == null) return;
        e.rimuoviProdotto(p);
    }
}
