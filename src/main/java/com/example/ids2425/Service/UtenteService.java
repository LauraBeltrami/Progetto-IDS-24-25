package com.example.ids2425.Service;

import com.example.ids2425.Model.Ruolo;
import com.example.ids2425.Model.UtenteGenerico;

public class UtenteService {

    public void assegnaRuolo(UtenteGenerico u, Ruolo r) {
        if (u == null || r == null) return;
        u.aggiungiRuolo(r);
    }

    public void rimuoviRuolo(UtenteGenerico u, Ruolo r) {
        if (u == null || r == null) return;
        u.rimuoviRuolo(r);
    }

    public boolean haRuolo(UtenteGenerico u, Ruolo r) {
        if (u == null || r == null) return false;
        return u.haRuolo(r instanceof Enum ? ((Enum<?>) r).name() : r.toString());
        // se Ruolo è un enum e hai cambiato UtenteGenerico per usare direttamente enum, allora:
        // return u.getRuoli().contains(r);
    }
}

