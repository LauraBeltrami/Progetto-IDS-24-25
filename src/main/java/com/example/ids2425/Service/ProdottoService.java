package com.example.ids2425.Service;

import com.example.ids2425.Model.Prodotto;
import com.example.ids2425.Model.Venditore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProdottoService {

    public Prodotto creaProdotto(int id, String nome, double prezzo, Venditore venditore) {
        Prodotto p = new Prodotto(id, nome, prezzo);
        // se nel tuo Model hai un campo venditore dentro Prodotto, impostalo qui
        // p.setVenditore(venditore);
        return p;
    }

    public void aggiornaPrezzo(Prodotto p, double nuovoPrezzo) {
        if (nuovoPrezzo <= 0) throw new IllegalArgumentException("Prezzo non valido");
        p.setPrezzo(nuovoPrezzo);
    }

    public void aggiornaNome(Prodotto p, String nuovoNome) {
        if (nuovoNome == null || nuovoNome.isBlank()) throw new IllegalArgumentException("Nome non valido");
        p.setNome(nuovoNome);
    }

    public void assegnaVenditore(Prodotto p, Venditore v) {
        // se il tuo Model prevede il venditore sul prodotto, setta qui.
        // p.setVenditore(v);
    }

    public void aggiungiAlCatalogo(List<Prodotto> catalogo, Prodotto p) {
        if (catalogo == null) return;
        if (catalogo.stream().noneMatch(x -> x.getId() == p.getId())) {
            catalogo.add(p);
        }
    }

    public void rimuoviDaCatalogo(List<Prodotto> catalogo, Prodotto p) {
        if (catalogo == null) return;
        catalogo.removeIf(x -> x.getId() == p.getId());
    }

    public List<Prodotto> cerca(List<Prodotto> catalogo, String testo) {
        if (catalogo == null) return List.of();
        if (testo == null || testo.isBlank()) return new ArrayList<>(catalogo);
        String t = testo.toLowerCase(Locale.ITALIAN);
        return catalogo.stream()
                .filter(p -> p.getNome() != null && p.getNome().toLowerCase(Locale.ITALIAN).contains(t))
                .collect(Collectors.toList());
    }

    public List<Prodotto> prodottiDiVenditore(List<Prodotto> catalogo, Venditore v) {
        if (catalogo == null || v == null) return List.of();
        // se hai il campo venditore su Prodotto, filtra per quello
        // return catalogo.stream().filter(p -> p.getVenditore()!=null && p.getVenditore().getId()==v.getId()).toList();
        return List.of(); // rimuovi quando avrai il campo nel Model
    }
}

