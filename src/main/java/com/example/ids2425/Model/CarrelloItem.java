package com.example.ids2425.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "carrello_items")
public class CarrelloItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrello_id")
    private Carrello carrello;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodotto_id")
    private Prodotto prodotto;

    @Column(nullable = false)
    private int quantita;

    // === Costruttore richiesto da JPA (obbligatorio!) ===
    protected CarrelloItem() { }

    // === Costruttore di comodo usato dal controller/service ===
    public CarrelloItem(Integer id, Carrello carrello, Prodotto prodotto, int quantita) {
        this.id = id;
        this.carrello = carrello;
        this.prodotto = prodotto;
        this.quantita = quantita;
    }
    public CarrelloItem(Carrello carrello, Prodotto prodotto, int quantita) {
        this(null, carrello, prodotto, quantita);
    }

    // === Getter / Setter ===
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Carrello getCarrello() { return carrello; }
    public void setCarrello(Carrello carrello) { this.carrello = carrello; }

    public Prodotto getProdotto() { return prodotto; }
    public void setProdotto(Prodotto prodotto) { this.prodotto = prodotto; }

    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }

    // === Valori derivati (non persistiti) ===
    @Transient
    public double getPrezzoUnitario() {
        return (prodotto != null) ? prodotto.getPrezzo() : 0.0;
    }

    @Transient
    public double getTotaleRiga() {
        return getPrezzoUnitario() * quantita;
    }
}