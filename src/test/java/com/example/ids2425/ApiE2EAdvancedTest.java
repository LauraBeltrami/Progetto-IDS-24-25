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

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * E2E avanzati su H2 in-memory.
 * - Flusso evento con prenotazioni
 * - Edge cases su API evento/carrello
 * - Logica service e model (senza repository)
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ApiE2EAdvancedTest {

    @Autowired MockMvc mvc;
    ObjectMapper om = new ObjectMapper();

    // ----------- EVENTO: creazione, prenotazione acquirente/venditore -----------
    @Test
    void evento_flow() throws Exception {
        // crea evento
        Date now = new Date();
        var evRes = mvc.perform(post("/api/eventi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titolo":"Sagra delle Mele","descrizione":"Festa delle mele","data":%d}
                                """.formatted(now.getTime())))
                .andExpect(status().isCreated())
                .andReturn();
        int eventoId = om.readTree(evRes.getResponse().getContentAsString())
                .get("id").asInt();

        // crea acquirente e venditore
        int acqId = om.readTree(mvc.perform(post("/api/acquirenti")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"nome\":\"Anna\",\"cognome\":\"Bianchi\",\"email\":\"anna@b.com\"}"))
                        .andReturn().getResponse().getContentAsString())
                .get("id").asInt();

        int venId = om.readTree(mvc.perform(post("/api/venditori")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"nome\":\"FruttaVerde\"}"))
                        .andReturn().getResponse().getContentAsString())
                .get("id").asInt();

        // prenota acquirente e venditore sull'evento
        mvc.perform(post("/api/acquirenti/{id}/prenotaEvento/{eventoId}", acqId, eventoId))
                .andExpect(status().isOk());

        mvc.perform(post("/api/venditori/{id}/prenotaEvento/{eventoId}", venId, eventoId))
                .andExpect(status().isOk());

        // recupera evento aggiornato
        String eventoJsonStr = mvc.perform(get("/api/eventi/{id}", eventoId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode eventoNode = om.readTree(eventoJsonStr);

        // verifica che i nodi esistano
        JsonNode acquirentiNode = eventoNode.get("acquirentiEmails");
        JsonNode venditoriNode = eventoNode.get("venditoriNomi");

        assertThat(acquirentiNode).isNotNull();
        assertThat(venditoriNode).isNotNull();

        // verifica presenza dati corretti
        assertThat(acquirentiNode.toString()).contains("anna@b.com");
        assertThat(venditoriNode.toString()).contains("FruttaVerde");
    }

    // ----------- EDGE CASES: prenotazione evento inesistente, quantità negativa -----------
    @Test
    void edge_cases() throws Exception {
        // prenotazione evento inesistente
        int acqId = om.readTree(mvc.perform(post("/api/acquirenti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"X\",\"cognome\":\"Y\",\"email\":\"x@y.com\"}"))
                .andReturn().getResponse().getContentAsString()).get("id").asInt();
        mvc.perform(post("/api/acquirenti/{id}/prenotaEvento/9999", acqId))
                .andExpect(status().isBadRequest());

        // aggiunta prodotto con quantità negativa
        int vendId = om.readTree(mvc.perform(post("/api/venditori")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Errata\"}"))
                .andReturn().getResponse().getContentAsString()).get("id").asInt();
        int prodId = om.readTree(mvc.perform(post("/api/prodotti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Banana\",\"prezzo\":1.1,\"venditoreId\":%d}".formatted(vendId)))
                .andReturn().getResponse().getContentAsString()).get("id").asInt();
        int acq2Id = om.readTree(mvc.perform(post("/api/acquirenti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Q\",\"cognome\":\"Z\",\"email\":\"q@z.com\"}"))
                .andReturn().getResponse().getContentAsString()).get("id").asInt();
        int cartId = om.readTree(mvc.perform(post("/api/acquirenti/{id}/carrello", acq2Id))
                .andReturn().getResponse().getContentAsString()).get("id").asInt();
        mvc.perform(post("/api/carrelli/{id}/items", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prodottoId\":%d,\"quantita\":-3}".formatted(prodId)))
                .andExpect(status().isBadRequest());
    }

    // ----------- LOGICA MODEL/SERVICE: test su metodi di dominio (no repo, puro Java) -----------
    @Test
    void model_and_service_logic() {
        // CertificazioneService
        var prod = new com.example.ids2425.Model.Prodotto(99, "Kiwi", 2.0);
        var cur = new com.example.ids2425.Model.Curatore(11, "Dott. Test");
        var certServ = new com.example.ids2425.Service.CertificazioneService();
        var cert = certServ.creaCertificazione(1, "Bio", prod, cur);
        assertThat(cert.getDescrizione()).isEqualTo("Bio");
        certServ.cambiaDescrizione(cert, "Bio premium");
        assertThat(cert.getDescrizione()).isEqualTo("Bio premium");
        certServ.assegnaAProdotto(cert, prod);
        assertThat(cert.getProdotto()).isEqualTo(prod);

        // Utente/Ruolo
        var u = new com.example.ids2425.Model.UtenteGenerico(10, "Mario", "mario@mail.com");
        var utenteService = new com.example.ids2425.Service.UtenteService();
        utenteService.assegnaRuolo(u, com.example.ids2425.Model.Ruolo.ACQUIRENTE);
        assertThat(u.haRuolo(com.example.ids2425.Model.Ruolo.ACQUIRENTE)).isTrue();
        utenteService.rimuoviRuolo(u, com.example.ids2425.Model.Ruolo.ACQUIRENTE);
        assertThat(u.haRuolo(com.example.ids2425.Model.Ruolo.ACQUIRENTE)).isFalse();

        // Curatore/Certificazione
        var curatore = new com.example.ids2425.Model.Curatore(7, "Prof. Rosa");
        var prodotto = new com.example.ids2425.Model.Prodotto(2, "Pesca", 1.3);
        var certificazione = new com.example.ids2425.Model.Certificazione(1, "DOC", prodotto, curatore);
        assertThat(certificazione.getCuratoreValidatore()).isEqualTo(curatore);
        certificazione.setDescrizione("IGP");
        assertThat(certificazione.getDescrizione()).isEqualTo("IGP");
    }
}
