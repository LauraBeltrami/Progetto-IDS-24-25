package com.example.ids2425.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "eventi")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nomeEvento;
    private String luogo;
    private LocalDateTime data;
    private String descrizione;

    // prodotti collegati all'evento
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "evento_prodotto",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "prodotto_id")
    )
    private List<Prodotto> prodotti;

    // --- NUOVI CAMPI PER INVITI E ACCETTAZIONI ---
    @ManyToMany
    @JoinTable(
            name = "evento_acquirenti",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "acquirente_id")
    )
    private List<Acquirente> acquirentiInvitati = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "evento_venditori",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "venditore_id")
    )
    private List<Venditore> venditoriInvitati = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "evento_accettazioni_acquirenti", joinColumns = @JoinColumn(name = "evento_id"))
    @MapKeyJoinColumn(name = "acquirente_id")
    @Column(name = "accettato")
    private Map<Acquirente, Boolean> accettazioniAcquirenti = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "evento_accettazioni_venditori", joinColumns = @JoinColumn(name = "evento_id"))
    @MapKeyJoinColumn(name = "venditore_id")
    @Column(name = "accettato")
    private Map<Venditore, Boolean> accettazioniVenditori = new HashMap<>();

    // --- COSTRUTTORI ---
    public Evento() {
        this.prodotti = new ArrayList<>();
    }

    public Evento(int id, String nomeEvento, String luogo, LocalDateTime data, String descrizione) {
        this.id = id;
        this.nomeEvento = nomeEvento;
        this.luogo = luogo;
        this.data = data;
        this.descrizione = descrizione;
        this.prodotti = new ArrayList<>();
    }

    // --- GETTER / SETTER ---
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

    // --- METODI PER PRODOTTI ---
    public void aggiungiProdotto(Prodotto p) {
        if (p == null) return;
        for (Prodotto x : prodotti) {
            if (x.getId() == p.getId()) return;
        }
        prodotti.add(p);
    }

    public void rimuoviProdotto(Prodotto p) {
        if (p == null) return;
        prodotti.removeIf(x -> x.getId() == p.getId());
    }

    // --- METODI PER INVITI E ACCETTAZIONI ---
    public void invitaAcquirente(Acquirente a) {
        if (!acquirentiInvitati.contains(a)) {
            acquirentiInvitati.add(a);
            accettazioniAcquirenti.put(a, false);
        }
    }

    public void invitaVenditore(Venditore v) {
        if (!venditoriInvitati.contains(v)) {
            venditoriInvitati.add(v);
            accettazioniVenditori.put(v, false);
        }
    }

    public void accettaInvitoAcquirente(Acquirente a) {
        if (accettazioniAcquirenti.containsKey(a)) {
            accettazioniAcquirenti.put(a, true);
        }
    }

    public void accettaInvitoVenditore(Venditore v) {
        if (accettazioniVenditori.containsKey(v)) {
            accettazioniVenditori.put(v, true);
        }
    }

    // --- GETTER PER LISTE E MAPPE ---
    public List<Acquirente> getAcquirentiInvitati() { return acquirentiInvitati; }
    public List<Venditore> getVenditoriInvitati() { return venditoriInvitati; }
    public Map<Acquirente, Boolean> getAccettazioniAcquirenti() { return accettazioniAcquirenti; }
    public Map<Venditore, Boolean> getAccettazioniVenditori() { return accettazioniVenditori; }
}
