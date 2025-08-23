package com.example.ids2425.Model;

public class GestorePiattaforma {

    private int id;
    private String nome;
    private String stato; // es: "ATTIVO", "SOSPESO"

    public GestorePiattaforma(int id, String nome, String stato) {
        this.id = id;
        this.nome = nome;
        this.stato = stato;
    }

    // getter/setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    // azioni sul profilo utente
    public void assegnaRuolo(UtenteGenerico utente, Ruolo ruolo) {
        if (utente != null && ruolo != null) {
            utente.aggiungiRuolo(ruolo);
        }
    }

    public void rimuoviRuolo(UtenteGenerico utente, Ruolo ruolo) {
        if (utente != null && ruolo != null) {
            utente.rimuoviRuolo(ruolo);
        }
    }
}


