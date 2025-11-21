package com.foodify.server.modules.identity.repository;

import com.foodify.server.modules.identity.domain.DriverDocument;
import com.foodify.server.modules.identity.domain.DriverDocumentStatus;
import com.foodify.server.modules.identity.domain.DriverDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverDocumentRepository extends JpaRepository<DriverDocument, Long> {
    List<DriverDocument> findAllByDriver_Id(Long driverId);
    Optional<DriverDocument> findByDriver_IdAndType(Long driverId, DriverDocumentType type);
    long countByDriver_IdAndStatus(Long driverId, DriverDocumentStatus status);
}
