package com.foodify.server.modules.identity.application;

import com.foodify.server.modules.identity.domain.Client;

public interface ClientDirectoryService {

    Client getClientOrThrow(Long clientId);
}
