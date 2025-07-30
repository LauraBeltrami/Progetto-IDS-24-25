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
import static org.assertj.core.data.Offset.offset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * E2E tests su H2 in-memory.
 * - venditoreEProdotto_CRUD_flow
 * - acquirente_CRUD
 * - carrello_flow
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // DB pulito a ogni test
class ApiE2ETest {

    @Autowired
    MockMvc mvc;

    ObjectMapper om = new ObjectMapper();

    // ----------- TEST 1: VENDITORE + PRODOTTO -----------
    @Test
    void venditoreEProdotto_CRUD_flow() throws Exception {
        // 1) CREA VENDITORE
        var vendRes = mvc.perform(post("/api/venditori")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Azienda Agricola Rossi\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        int venditoreId = om.readTree(vendRes.getResponse().getContentAsString()).get("id").asInt();

        // 2) CREA PRODOTTO associato
        var prodRes = mvc.perform(post("/api/prodotti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(("{\"nome\":\"Mela\",\"prezzo\":1.2,\"venditoreId\":%d}").formatted(venditoreId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        int prodottoId = om.readTree(prodRes.getResponse().getContentAsString()).get("id").asInt();

        // 3) LISTA PRODOTTI (controllo presenza per id, NON per indice)
        var listAll = mvc.perform(get("/api/prodotti"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode all = om.readTree(listAll.getResponse().getContentAsString());
        assertThat(containsId(all, prodottoId)).isTrue();

        // 3-bis) LISTA PRODOTTI DEL VENDITORE (sempre per id)
        var listVend = mvc.perform(get("/api/venditori/{id}/prodotti", venditoreId))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode vendProducts = om.readTree(listVend.getResponse().getContentAsString());
        assertThat(containsId(vendProducts, prodottoId)).isTrue();

        // 4) UPDATE PRODOTTO
        mvc.perform(put("/api/prodotti/{id}", prodottoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(("{\"nome\":\"Mela Bio\",\"prezzo\":1.4,\"venditoreId\":%d}").formatted(venditoreId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Mela Bio"))
                .andExpect(jsonPath("$.prezzo").value(1.4));

        // 5) DELETE PRODOTTO
        mvc.perform(delete("/api/prodotti/{id}", prodottoId))
                .andExpect(status().isNoContent());

        // 6) VERIFICA NON PIÙ PRESENTE
        var listAfter = mvc.perform(get("/api/prodotti"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode after = om.readTree(listAfter.getResponse().getContentAsString());
        assertThat(containsId(after, prodottoId)).isFalse();
    }

    // ----------- TEST 2: ACQUIRENTE -----------
    @Test
    void acquirente_CRUD() throws Exception {
        // CREATE
        var create = mvc.perform(post("/api/acquirenti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Mario\",\"cognome\":\"Rossi\",\"email\":\"mario.rossi@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        int id = om.readTree(create.getResponse().getContentAsString()).get("id").asInt();

        // GET
        mvc.perform(get("/api/acquirenti/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("mario.rossi@example.com"));

        // DELETE
        mvc.perform(delete("/api/acquirenti/{id}", id))
                .andExpect(status().isNoContent());
    }

    // ----------- TEST 3: CARRELLO -----------
    @Test
    void carrello_flow() throws Exception {
        // acquirente
        int acquirenteId = om.readTree(mvc.perform(post("/api/acquirenti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Luca\",\"cognome\":\"Bianchi\",\"email\":\"luca@example.com\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()).get("id").asInt();

        // venditore
        int vendId = om.readTree(mvc.perform(post("/api/venditori")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Azienda Verdi\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()).get("id").asInt();

        // prodotto
        int prodId = om.readTree(mvc.perform(post("/api/prodotti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(("{\"nome\":\"Pera\",\"prezzo\":1.3,\"venditoreId\":%d}").formatted(vendId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()).get("id").asInt();

        // crea carrello
        var cartRes = mvc.perform(post("/api/acquirenti/{id}/carrello", acquirenteId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.acquirenteId").value(acquirenteId))
                .andReturn();
        int carrelloId = om.readTree(cartRes.getResponse().getContentAsString()).get("id").asInt();

        // aggiungi 2 pere → verifica numeroArticoli e totale con tolleranza
        var afterAdd = mvc.perform(post("/api/carrelli/{id}/items", carrelloId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(("{\"prodottoId\":%d,\"quantita\":2}").formatted(prodId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroArticoli").value(2))
                .andReturn();
        double tot = om.readTree(afterAdd.getResponse().getContentAsString()).get("totale").asDouble();
        assertThat(tot).isCloseTo(2.6, offset(1e-6));

        // rimuovi il prodotto → carrello vuoto
        mvc.perform(delete("/api/carrelli/{id}/items/{prod}", carrelloId, prodId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroArticoli").value(0))
                .andExpect(jsonPath("$.totale").value(0.0));
    }

    // ----------- helper -----------
    private boolean containsId(JsonNode array, int id) {
        if (!array.isArray()) return false;
        for (JsonNode n : array) {
            if (n.has("id") && n.get("id").asInt() == id) return true;
        }
        return false;
    }
}
