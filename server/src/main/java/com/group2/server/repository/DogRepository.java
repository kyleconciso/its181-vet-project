package com.group2.server.repository;

import com.group2.server.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Long> {
    List<Dog> findByIsAvailable(boolean isAvailable);
}