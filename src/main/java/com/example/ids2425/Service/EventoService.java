package com.example.ids2425.Service;

import com.example.ids2425.Model.Acquirente;
import com.example.ids2425.Model.Evento;
import com.example.ids2425.Model.Venditore;
import com.example.ids2425.Repository.AcquirenteRepository;
import com.example.ids2425.Repository.EventoRepository;
import com.example.ids2425.Repository.VenditoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepo;

    @Autowired
    private AcquirenteRepository acquirenteRepo;

    @Autowired
    private VenditoreRepository venditoreRepo;

    /**
     * Prenota un acquirente per un evento.
     * @param eventoId ID dell'evento
     * @param acquirenteId ID dell'acquirente
     * @return true se la prenotazione è avvenuta con successo, false altrimenti
     */
    @Transactional
    public boolean prenotaEventoAcquirente(int eventoId, int acquirenteId) {
        Evento evento = eventoRepo.findById(eventoId).orElse(null);
        Acquirente acquirente = acquirenteRepo.findById(acquirenteId).orElse(null);
        if (evento == null || acquirente == null) return false;

        // Aggiunge l'acquirente all'evento in sicurezza
        if (!evento.getAcquirenti().contains(acquirente)) {
            evento.getAcquirenti().add(acquirente);
        }

        eventoRepo.save(evento);
        return true;
    }

    /**
     * Prenota un venditore per un evento.
     * @param eventoId ID dell'evento
     * @param venditoreId ID del venditore
     * @return true se la prenotazione è avvenuta con successo, false altrimenti
     */
    @Transactional
    public boolean prenotaEventoVenditore(int eventoId, int venditoreId) {
        Evento evento = eventoRepo.findById(eventoId).orElse(null);
        Venditore venditore = venditoreRepo.findById(venditoreId).orElse(null);
        if (evento == null || venditore == null) return false;

        // Aggiunge il venditore all'evento in sicurezza
        if (!evento.getVenditori().contains(venditore)) {
            evento.getVenditori().add(venditore);
        }

        eventoRepo.save(evento);
        return true;
    }
}
