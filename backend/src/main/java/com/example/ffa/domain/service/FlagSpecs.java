package com.example.ffa.domain.service;

import com.example.ffa.domain.models.Flag;
import org.springframework.data.jpa.domain.Specification;

public class FlagSpecs {
    public static Specification<Flag> q(String q) {
        if (q == null || q.isBlank()) return null;
        var like = "%" + q.toLowerCase() + "%";
        return (root, cq, cb) -> cb.or(
                cb.like(cb.lower(root.get("key")), like),
                cb.like(cb.lower(root.get("name")), like),
                cb.like(cb.lower(root.get("description")), like)
        );
    }

    public static Specification<Flag> enabled(Boolean enabled) {
        if (enabled == null) return null;
        return (root, cq, cb) -> cb.equal(root.get("enabled"), enabled);
    }
}