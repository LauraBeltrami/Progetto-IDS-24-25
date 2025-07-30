package com.example.ids2425.Service;

import com.example.ids2425.Model.Acquirente;
import com.example.ids2425.Model.Pagamento;

public class PagamentoService {

    public Pagamento creaPagamento(int id, Acquirente acquirente, double importo, String metodo) {
        if (importo <= 0) throw new IllegalArgumentException("Importo non valido");
        return new Pagamento(id, acquirente, importo, metodo);
    }

    public void completa(Pagamento p) {
        p.completa();
    }

    public void fallisce(Pagamento p) {
        p.fallisce();
    }

    public void annulla(Pagamento p) {
        p.setStato("ANNULLATO");
    }
}

