package com.jxjtech.yakmanager.service;

import com.jxjtech.yakmanager.entity.ServerStatusEntity;
import com.jxjtech.yakmanager.repository.ServerStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerStatusRepository serverStatusRepository;

    public ServerStatusEntity getServerStatus() {
        return serverStatusRepository.findFirst().get();
    }
}
