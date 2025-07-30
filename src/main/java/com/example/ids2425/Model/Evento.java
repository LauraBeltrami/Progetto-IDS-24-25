package com.example.ids2425.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Evento {

    private int id;
    private String nomeEvento;
    private String luogo;
    private LocalDateTime data;
    private String descrizione;

    // prodotti collegati all'evento
    private List<Prodotto> prodotti;

    public Evento(int id, String nomeEvento, String luogo, LocalDateTime data, String descrizione) {
        this.id = id;
        this.nomeEvento = nomeEvento;
        this.luogo = luogo;
        this.data = data;
        this.descrizione = descrizione;
        this.prodotti = new ArrayList<>();
    }

    // getter e setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomeEvento() { return nomeEvento; }
    public void setNomeEvento(String nomeEvento) { this.nomeEvento = nomeEvento; }

    public String getLuogo() { return luogo; }
    public void setLuogo(String luogo) { this.luogo = luogo; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public List<Prodotto> getProdotti() { return prodotti; }
    public void setProdotti(List<Prodotto> prodotti) { this.prodotti = prodotti; }

    // utilità
    public void aggiungiProdotto(Prodotto p) {
        if (p == null) return;
        // evita duplicati per id
        for (Prodotto x : prodotti) {
            if (x.getId() == p.getId()) return;
        }
        prodotti.add(p);
    }

    public void rimuoviProdotto(Prodotto p) {
        if (p == null) return;
        prodotti.removeIf(x -> x.getId() == p.getId());
    }
}

