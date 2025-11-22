package com.foodify.server.modules.admin.client.repository;

import com.foodify.server.modules.identity.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
}
