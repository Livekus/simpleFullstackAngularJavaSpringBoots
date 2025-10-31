package com.example.ffa.adapter.controller;


import com.example.ffa.ApiErrorHandler;
import com.example.ffa.adapter.dtos.FlagDtos;
import com.example.ffa.application.service.FlagService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FlagController.class)
@Import(ApiErrorHandler.class)
public class FlagControllerTest {

    @Autowired MockMvc mvc;
    @MockBean
    FlagService service;

    private FlagDtos.FlagDto dto(UUID id, String key, int version, boolean enabled) {
        Instant now = Instant.parse("2025-10-31T11:11:12.257124Z");
        return new FlagDtos.FlagDto(id, key, "Name", "Desc", enabled, new String[]{"web"}, "bom", version, now, "demo", now, "demo");
    }

    @Test
    void getById_returns200_andETag_andCacheControl() throws Exception {
        UUID id = UUID.fromString("9f31d8da-56df-4152-8b20-7cd3be8a9aad");
        FlagDtos.FlagDto f = dto(id, "beta_ui", 1, true);
        Mockito.when(service.get(id)).thenReturn(f);

        mvc.perform(get("/api/v1/flags/{id}", id))
                .andExpect(status().isOk())
                .andExpect(header().string("ETag", "\"1\""))
                .andExpect(header().string("Cache-Control", org.hamcrest.Matchers.containsString("max-age=60")))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.version").value(1))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void list_returns200_withPaging_andCacheControl() throws Exception {
        FlagDtos.FlagDto f1 = dto(UUID.randomUUID(), "feat_1", 0, false);
        FlagDtos.FlagDto f2 = dto(UUID.randomUUID(), "feat_2", 0, true);
        Page<FlagDtos.FlagDto> page = new PageImpl<>(List.of(f1, f2), PageRequest.of(0,2), 4);
        Mockito.when(service.search(eq("feat_"), eq(null), any(Pageable.class))).thenReturn(page);

        mvc.perform(get("/api/v1/flags").param("q","feat_").param("size","2").param("page","0"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", org.hamcrest.Matchers.containsString("max-age=30")))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(4))
                .andExpect(jsonPath("$.content[0].key").value("feat_1"));
    }

    @Test
    void create_returns201() throws Exception {
        UUID id = UUID.randomUUID();
        FlagDtos.FlagDto f = dto(id, "beta_ui_1234", 0, true);
        Mockito.when(service.create(any(FlagDtos.CreateFlagReq.class), anyString())).thenReturn(f);

        String body = """
      {
        "key":"beta_ui_1234",
        "name":"Beta UI",
        "description":"Beta rollout",
        "enabled":true,
        "tags":["web","beta"],
        "owner":"bom"
      }
      """;

        mvc.perform(post("/api/v1/flags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Actor","demo-user")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.key").value("beta_ui_1234"));
    }

    @Test
    void update_returns200_andVersionIncrement() throws Exception {
        UUID id = UUID.randomUUID();
        FlagDtos.FlagDto updated = dto(id, "beta_ui_1234", 1, false);
        Mockito.when(service.update(eq(id), any(FlagDtos.UpdateFlagReq.class), anyString())).thenReturn(updated);

        String body = """
      { "version":0, "name":"Beta UI (updated)", "enabled":false, "tags":["web","toggled"], "owner":"bom" }
      """;

        mvc.perform(put("/api/v1/flags/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Actor","demo-user")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(1))
                .andExpect(jsonPath("$.enabled").value(false));
    }

    @Test
    void update_withStaleVersion_returns409() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(service.update(eq(id), any(FlagDtos.UpdateFlagReq.class), anyString()))
                .thenThrow(new IllegalStateException("Version mismatch"));

        String body = """
      { "version":0, "name":"Should fail" }
      """;

        mvc.perform(put("/api/v1/flags/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Optimistic lock conflict"));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(service.get(id)).thenThrow(new NoSuchElementException("Flag not found"));

        mvc.perform(get("/api/v1/flags/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_returns204() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/flags/{id}", id)
                        .header("X-Actor","demo-user"))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(eq(id), anyString());
    }
}