package com.example.ids2425;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EventoE2ETest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper om = new ObjectMapper();

    @Test
    void eventoCompletoConAccettazioni() throws Exception {
        // 1️⃣ Crea venditore
        String venditoreJson = "{\"nome\":\"Azienda Verde\"}";
        int vendId = om.readTree(
                mvc.perform(post("/api/venditori")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(venditoreJson))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        ).get("id").asInt();

        // 2️⃣ Crea acquirente
        String acquirenteJson = "{\"nome\":\"Paolo\",\"cognome\":\"Neri\",\"email\":\"paolo@example.com\"}";
        int acqId = om.readTree(
                mvc.perform(post("/api/acquirenti")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(acquirenteJson))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        ).get("id").asInt();

        // 3️⃣ Crea prodotto
        String prodottoJson = "{\"nome\":\"Mela Bio\",\"prezzo\":1.5,\"venditoreId\":" + vendId + "}";
        int prodId = om.readTree(
                mvc.perform(post("/api/prodotti")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(prodottoJson))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        ).get("id").asInt();

        // 4️⃣ Crea evento
        String dataEvento = "2025-09-01T10:00:00";
        String eventoJson = String.format(
                "{\"nomeEvento\":\"Festa Autunno\",\"luogo\":\"Parco\",\"data\":\"%s\",\"descrizione\":\"Evento di prova\"}",
                dataEvento
        );

        int eventoId = om.readTree(
                mvc.perform(post("/api/eventi")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(eventoJson))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").exists())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        ).get("id").asInt();

        // 5️⃣ Aggiungi prodotto all'evento
        mvc.perform(post("/api/eventi/{id}/prodotti/{prodId}", eventoId, prodId))
                .andExpect(status().isOk());

        // 6️⃣ Invita acquirente e venditore
        mvc.perform(post("/api/eventi/{id}/invita/acquirente/{acqId}", eventoId, acqId))
                .andExpect(status().isOk());
        mvc.perform(post("/api/eventi/{id}/invita/venditore/{vendId}", eventoId, vendId))
                .andExpect(status().isOk());

        // 7️⃣ Accetta inviti
        mvc.perform(post("/api/eventi/{id}/accetta/acquirente/{acqId}", eventoId, acqId))
                .andExpect(status().isOk());
        mvc.perform(post("/api/eventi/{id}/accetta/venditore/{vendId}", eventoId, vendId))
                .andExpect(status().isOk());

        // 8️⃣ Controlla stato evento
        JsonNode eventoNode = om.readTree(
                mvc.perform(get("/api/eventi/{id}", eventoId))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        // Controlla prodotto
        boolean hasProd = false;
        for (JsonNode p : eventoNode.path("prodotti")) {
            if (p.path("id").asInt() == prodId) hasProd = true;
        }
        assertThat(hasProd).isTrue();

        // Controlla accettazioni
        assertThat(eventoNode.path("accettazioniAcquirenti").path(String.valueOf(acqId)).asBoolean()).isTrue();
        assertThat(eventoNode.path("accettazioniVenditori").path(String.valueOf(vendId)).asBoolean()).isTrue();
    }
}
