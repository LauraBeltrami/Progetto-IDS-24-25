package com.example.ids2425.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventi")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String luogo;
    private LocalDateTime data;
    private String descrizione;

    // Relazione con i prodotti (se già esiste, lasciala com’è)
    @ManyToMany
    @JoinTable(
            name = "evento_prodotti",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "prodotto_id")
    )
    private List<Prodotto> prodotti = new ArrayList<>();

    // 🔹 Relazione con gli invitati
    @ManyToMany
    @JoinTable(
            name = "evento_invitati",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "utente_id")
    )
    private List<UtenteGenerico> invitati = new ArrayList<>();

    public Evento() {}

    public Evento(int id, String nome, String luogo, LocalDateTime data, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.luogo = luogo;
        this.data = data;
        this.descrizione = descrizione;
    }

    // --- getter/setter ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLuogo() { return luogo; }
    public void setLuogo(String luogo) { this.luogo = luogo; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public List<Prodotto> getProdotti() { return prodotti; }
    public void setProdotti(List<Prodotto> prodotti) { this.prodotti = prodotti; }

    public List<UtenteGenerico> getInvitati() { return invitati; }
    public void setInvitati(List<UtenteGenerico> invitati) { this.invitati = invitati; }

    // --- Metodi di supporto ---
    public void aggiungiProdotto(Prodotto p) {
        if (p != null && !prodotti.contains(p)) {
            prodotti.add(p);
        }
    }

    public void rimuoviProdotto(Prodotto p) {
        prodotti.remove(p);
    }

    public void invitaUtente(UtenteGenerico u) {
        if (u != null && !invitati.contains(u)) {
            invitati.add(u);
        }
    }

    public void rimuoviInvitato(UtenteGenerico u) {
        invitati.remove(u);
    }

}
