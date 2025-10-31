package com.example.ffa.application.repository;

import com.example.ffa.domain.models.Flag;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface FlagRepository extends JpaRepository<Flag, UUID>, JpaSpecificationExecutor<Flag> {
    Optional<Flag> findByKey(String key);
}