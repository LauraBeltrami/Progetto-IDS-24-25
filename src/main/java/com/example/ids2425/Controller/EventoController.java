package com.example.ids2425.Controller;

import com.example.ids2425.EventoDTO;
import com.example.ids2425.Model.Evento;
import com.example.ids2425.Repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepo;

    // ----------------- CREAZIONE EVENTO -----------------
    @PostMapping
    public ResponseEntity<Evento> create(@RequestBody Evento evento) {
        Evento saved = eventoRepo.save(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ----------------- RECUPERA EVENTO PER ID -----------------
    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> getById(@PathVariable Integer id) {
        Optional<Evento> eventoOpt = eventoRepo.findById(id);
        if (eventoOpt.isEmpty()) return ResponseEntity.notFound().build();

        Evento evento = eventoOpt.get();
        EventoDTO dto = new EventoDTO();
        dto.id = evento.getId();
        dto.titolo = evento.getTitolo();
        dto.descrizione = evento.getDescrizione();
        dto.data = evento.getData();
        dto.acquirentiEmails = evento.getAcquirenti()
                .stream()
                .map(a -> a.getEmail())
                .collect(Collectors.toList());
        dto.venditoriNomi = evento.getVenditori()
                .stream()
                .map(v -> v.getNome())
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }

    // ----------------- RECUPERA TUTTI GLI EVENTI -----------------
    @GetMapping
    public ResponseEntity<List<EventoDTO>> getAll() {
        List<EventoDTO> dtos = eventoRepo.findAll().stream().map(evento -> {
            EventoDTO dto = new EventoDTO();
            dto.id = evento.getId();
            dto.titolo = evento.getTitolo();
            dto.descrizione = evento.getDescrizione();
            dto.data = evento.getData();
            dto.acquirentiEmails = evento.getAcquirenti()
                    .stream()
                    .map(a -> a.getEmail())
                    .collect(Collectors.toList());
            dto.venditoriNomi = evento.getVenditori()
                    .stream()
                    .map(v -> v.getNome())
                    .collect(Collectors.toList());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // ----------------- ELIMINA EVENTO -----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!eventoRepo.existsById(id)) return ResponseEntity.notFound().build();
        eventoRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
