package com.example.ids2425.DTO;

import java.math.BigDecimal;

public record RigaProdottoDTO(
        Long prodottoId,
        String nome,
        BigDecimal prezzoUnitario,
        int quantita,
        BigDecimal totaleRiga
) {}
