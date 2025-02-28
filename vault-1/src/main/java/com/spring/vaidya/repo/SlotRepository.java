package com.spring.vaidya.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.vaidya.entity.Slot;

/**
 * Repository interface for managing Slot entities.
 * Extends JpaRepository to provide built-in CRUD operations.
 */
public interface SlotRepository extends JpaRepository<Slot, Long> {

    // You can add custom query methods here if needed in the future.

}
