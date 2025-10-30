package com.example.ids2425.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record EventoDTO(
        Long id,
        String titolo,
        String descrizione,
        String luogo,
        LocalDateTime inizio,
        LocalDateTime fine,
        Long animatoreId,
        String animatoreNome,
        List<InvitoDTO> inviti
) {}
