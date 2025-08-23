package com.example.ids2425.Model;

import java.time.LocalDateTime;

public class Pagamento {

    private int id;
    private Acquirente acquirente;
    private double importoTotale;
    private String metodo;    // es: "Carta di credito", "PayPal"
    private String stato;     // es: "IN_ATTESA", "COMPLETATO", "FALLITO"
    private LocalDateTime dataOperazione;

    public Pagamento(int id, Acquirente acquirente, double importoTotale, String metodo) {
        this.id = id;
        this.acquirente = acquirente;
        this.importoTotale = importoTotale;
        this.metodo = metodo;
        this.stato = "IN_ATTESA";
        this.dataOperazione = LocalDateTime.now();
    }

    // getter e setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Acquirente getAcquirente() { return acquirente; }
    public void setAcquirente(Acquirente acquirente) { this.acquirente = acquirente; }

    public double getImportoTotale() { return importoTotale; }
    public void setImportoTotale(double importoTotale) { this.importoTotale = importoTotale; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public LocalDateTime getDataOperazione() { return dataOperazione; }
    public void setDataOperazione(LocalDateTime dataOperazione) { this.dataOperazione = dataOperazione; }

    // metodi di utilità
    public void completa() {
        this.stato = "COMPLETATO";
    }

    public void fallisce() {
        this.stato = "FALLITO";
    }
}

