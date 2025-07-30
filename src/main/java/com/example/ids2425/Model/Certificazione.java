package com.example.ids2425.Model;

public class Certificazione {

    private int id;
    private String descrizione;

    // riferimenti semplici ad oggetti del modello
    private Prodotto prodotto;
    private Curatore curatoreValidatore;

    public Certificazione(int id, String descrizione, Prodotto prodotto, Curatore curatoreValidatore) {
        this.id = id;
        this.descrizione = descrizione;
        this.prodotto = prodotto;
        this.curatoreValidatore = curatoreValidatore;
    }

    // getter e setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public Prodotto getProdotto() { return prodotto; }
    public void setProdotto(Prodotto prodotto) { this.prodotto = prodotto; }

    public Curatore getCuratoreValidatore() { return curatoreValidatore; }
    public void setCuratoreValidatore(Curatore curatoreValidatore) {
        this.curatoreValidatore = curatoreValidatore;
    }
}

