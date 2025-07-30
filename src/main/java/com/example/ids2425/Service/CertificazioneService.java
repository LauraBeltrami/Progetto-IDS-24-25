package com.example.ids2425.Service;

import com.example.ids2425.Model.Certificazione;
import com.example.ids2425.Model.Curatore;
import com.example.ids2425.Model.Prodotto;

public class CertificazioneService {

    public Certificazione creaCertificazione(int id, String descrizione, Prodotto prodotto, Curatore curatore) {
        return new Certificazione(id, descrizione, prodotto, curatore);
    }

    public void assegnaAProdotto(Certificazione c, Prodotto p) {
        if (c == null || p == null) return;
        c.setProdotto(p);
    }

    public void cambiaDescrizione(Certificazione c, String descrizione) {
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("Descrizione non valida");
        c.setDescrizione(descrizione);
    }
}

