package com.example.ffa.application.service;

import com.example.ffa.adapter.dtos.FlagDtos;
import com.example.ffa.adapter.mapper.FlagMapper;
import com.example.ffa.application.repository.AuditLogRepository;
import com.example.ffa.application.repository.FlagRepository;
import com.example.ffa.domain.models.AuditLog;
import com.example.ffa.domain.models.Flag;
import com.example.ffa.domain.service.FlagSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlagService {
    private final FlagRepository repo;
    private final FlagMapper mapper;
    private final AuditLogRepository audits;

    @Cacheable(cacheNames = "flagById", key = "#id")
    public FlagDtos.FlagDto get(UUID id) {
        return repo.findById(id).map(mapper::toDto).orElseThrow();
    }

    public Page<FlagDtos.FlagDto> search(String q, Boolean enabled, Pageable pageable) {
        Specification<Flag> spec = Specification.where((root, cq, cb) -> cb.conjunction());
        spec = spec.and(FlagSpecs.q(q)).and(FlagSpecs.enabled(enabled));
        return repo.findAll(spec, pageable).map(mapper::toDto);
    }

    @Transactional
    @CacheEvict(cacheNames = "flagById", key = "#result.id")
    public FlagDtos.FlagDto create(FlagDtos.CreateFlagReq req, String actor) {
        var f = mapper.toEntity(req);
        f.setCreatedBy(actor);
        f.setUpdatedBy(actor);
        var saved = repo.save(f);
        audits.save(AuditLog.builder()
                .entityType("Flag").entityId(saved.getId().toString()).action("CREATE")
                .actor(actor).at(Instant.now()).data(java.util.Map.of()).build());
        return mapper.toDto(saved);
    }

    @Transactional
    @CachePut(cacheNames = "flagById", key = "#id")
    public FlagDtos.FlagDto update(UUID id, FlagDtos.UpdateFlagReq req, String actor) {
        var f = repo.findById(id).orElseThrow();
        // optimistic concurrency via @Version
        if (!f.getVersion().equals(req.version())) throw new IllegalStateException("Version mismatch");
        mapper.update(f, req);
        f.setUpdatedAt(Instant.now());
        f.setUpdatedBy(actor);
        var saved = repo.save(f);
        audits.save(AuditLog.builder()
                .entityType("Flag").entityId(saved.getId().toString()).action("UPDATE")
                .actor(actor).at(Instant.now()).data(java.util.Map.of()).build());
        return mapper.toDto(saved);
    }

    @Transactional
    @CacheEvict(cacheNames = "flagById", key = "#id")
    public void delete(UUID id, String actor) {
        repo.findById(id).ifPresent(f -> {
            repo.delete(f);
            audits.save(AuditLog.builder().entityType("Flag").entityId(id.toString())
                    .action("DELETE").actor(actor).at(Instant.now()).data(java.util.Map.of()).build());
        });
    }
}