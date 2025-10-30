package com.example.ids2425.DTO;

import java.time.LocalDateTime;

public record CertificazioneDTO(
        Long id,
        String descrizione,
        Long curatoreId,
        String curatoreNome,
        LocalDateTime dataApprovazione
) {}
