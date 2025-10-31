package com.example.ffa.adapter.mapper;

import com.example.ffa.adapter.dtos.FlagDtos;
import com.example.ffa.domain.models.Flag;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FlagMapper {
    @Mappings({
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true)
    })
    FlagDtos.FlagDto toDto(Flag f);

    @Mappings({
            @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())"),
            @Mapping(target = "version", constant = "0"),
            @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())"),
            @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())"),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true)
    })
    Flag toEntity(FlagDtos.CreateFlagReq req);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "enabled", source = "enabled"),
            @Mapping(target = "tags", source = "tags"),
            @Mapping(target = "owner", source = "owner"),
            // explicitly ignore immutable / audit / id fields
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "key", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "version", ignore = true) // managed by JPA @Version
    })
    void update(@MappingTarget Flag f, FlagDtos.UpdateFlagReq req);
}
