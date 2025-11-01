package com.example.ffa.application.service;

import com.example.ffa.adapter.mapper.FlagMapper;
import com.example.ffa.application.repository.AuditLogRepository;
import com.example.ffa.application.repository.FlagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FlagServiceTest {

    @Test
    void search_withoutFilters_doesNotThrow_andDelegatesToRepo() {
        FlagRepository repo = mock(FlagRepository.class);
        AuditLogRepository audits = mock(AuditLogRepository.class);
        FlagMapper mapper = mock(FlagMapper.class);

        when(repo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(Page.empty());

        FlagService svc = new FlagService(repo, mapper, audits);

        assertDoesNotThrow(() -> svc.search(null, null, PageRequest.of(0, 10)));

        verify(repo, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
}