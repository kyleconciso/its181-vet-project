package com.group2.server.repository;

import com.group2.server.model.AdoptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, Long> {
    List<AdoptionRequest> findByUserId(Long userId);
}