package com.example.ffa.adapter.controller;

import com.example.ffa.adapter.dtos.FlagDtos;
import com.example.ffa.application.service.FlagService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flags")
public class FlagController {
    private final FlagService service;
    public FlagController(FlagService s){ this.service = s; }

    @GetMapping({"", "/"})
    public ResponseEntity<Page<FlagDtos.FlagDto>> list(
            @RequestParam(required=false) String q,
            @RequestParam(required=false) Boolean enabled,
            @PageableDefault(size=50, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        var page = service.search(q, enabled, pageable);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(java.time.Duration.ofSeconds(30)))
                .body(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlagDtos.FlagDto> get(@PathVariable("id") UUID id){
        var dto = service.get(id);
        return ResponseEntity.ok()
                .eTag('"' + dto.version().toString() + '"')
                .cacheControl(CacheControl.maxAge(java.time.Duration.ofSeconds(60)))
                .body(dto);
    }

    @PostMapping
    public ResponseEntity<FlagDtos.FlagDto> create(@RequestHeader(name="X-Actor", required=false) String actor,
                                                   @Valid @RequestBody FlagDtos.CreateFlagReq req){
        var dto = service.create(req, actor == null ? "demo-user" : actor);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public FlagDtos.FlagDto update(@RequestHeader(name="X-Actor", required=false) String actor,
                                   @PathVariable("id") UUID id, @Valid @RequestBody FlagDtos.UpdateFlagReq req){
        return service.update(id, req, actor == null ? "demo-user" : actor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader(name="X-Actor", required=false) String actor,
                       @PathVariable("id") UUID id){
        service.delete(id, actor == null ? "demo-user" : actor);
    }
}