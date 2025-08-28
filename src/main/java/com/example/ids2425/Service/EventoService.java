package com.example.ids2425.Service;

import com.example.ids2425.Model.Evento;
import com.example.ids2425.Model.Prodotto;
import com.example.ids2425.Model.Acquirente;
import com.example.ids2425.Model.Venditore;

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

    // --- NUOVI METODI PER INVITI E ACCETTAZIONE ---

    public void invitaAcquirente(Evento evento, Acquirente acquirente) {
        if (evento == null || acquirente == null) return;
        evento.invitaAcquirente(acquirente);
    }

    public void invitaVenditore(Evento evento, Venditore venditore) {
        if (evento == null || venditore == null) return;
        evento.invitaVenditore(venditore);
    }

    public void accettaInvitoAcquirente(Evento evento, Acquirente acquirente) {
        if (evento == null || acquirente == null) return;
        evento.accettaInvitoAcquirente(acquirente);
    }

    public void accettaInvitoVenditore(Evento evento, Venditore venditore) {
        if (evento == null || venditore == null) return;
        evento.accettaInvitoVenditore(venditore);
    }
}
