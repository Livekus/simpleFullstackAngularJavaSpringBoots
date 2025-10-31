package com.example.ffa.adapter.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;
import java.util.UUID;

public final class FlagDtos {
    public record FlagDto(UUID id, String key, String name, String description,
                          boolean enabled, String[] tags, String owner,
                          Integer version, Instant createdAt, String createdBy,
                          Instant updatedAt, String updatedBy
    ) {
    }

    public record CreateFlagReq(
            @NotBlank @Pattern(regexp = "^[a-z0-9_\\-]+$") String key,
            @NotBlank String name,
            String description,
            boolean enabled,
            String[] tags,
            String owner
    ) {
    }

    public record UpdateFlagReq(
            @NotNull Integer version,
            @NotBlank String name,
            String description,
            Boolean enabled,
            String[] tags,
            String owner
    ) {
    }
}
