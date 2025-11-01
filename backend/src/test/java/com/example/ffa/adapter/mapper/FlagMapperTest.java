package com.example.ffa.adapter.mapper;

import com.example.ffa.adapter.dtos.FlagDtos;
import com.example.ffa.domain.models.Flag;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class FlagMapperTest {
  FlagMapper mapper = Mappers.getMapper(FlagMapper.class);

  @Test
  void update_doesNotUnboxNull_forEnabled() {
    // entity has enabled=true initially
    Flag f = Flag.builder().enabled(true).build();

    // enabled omitted (null) in request
    FlagDtos.UpdateFlagReq req = new FlagDtos.UpdateFlagReq(0, "Name", null, null, null, null);

    assertDoesNotThrow(() -> mapper.update(f, req));
    assertTrue(f.isEnabled(), "enabled should be preserved when source is null");
  }

  @Test
  void update_setsEnabled_whenPresent() {
    Flag f = Flag.builder().enabled(true).build();
    FlagDtos.UpdateFlagReq req = new FlagDtos.UpdateFlagReq(0, "Name", null, Boolean.FALSE, null, null);

    mapper.update(f, req);
    assertFalse(f.isEnabled());
  }
}