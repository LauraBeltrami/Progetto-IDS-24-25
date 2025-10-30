package com.example.ids2425.Repository;

import com.example.ids2425.Model.InvitoEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitoEventoRepository extends JpaRepository<InvitoEvento, Long> {
    Optional<InvitoEvento> findByEventoIdAndVenditoreId(Long eventoId, Long venditoreId);
}