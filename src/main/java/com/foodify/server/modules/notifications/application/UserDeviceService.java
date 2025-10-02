package com.foodify.server.modules.notifications.application;

import com.foodify.server.modules.notifications.dto.DeviceRegisterRequest;
import com.foodify.server.modules.notifications.dto.DeviceUnregisterRequest;
import com.foodify.server.modules.identity.domain.User;
import com.foodify.server.modules.notifications.domain.UserDevice;
import com.foodify.server.modules.notifications.repository.UserDeviceRepository;
import com.foodify.server.modules.identity.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserDeviceService {
    private final UserDeviceRepository deviceRepo;
    private final UserRepository userRepo;

    public UserDeviceService(UserDeviceRepository deviceRepo, UserRepository userRepo, UserDeviceRepository userDeviceRepository) {
        this.deviceRepo = deviceRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void registerDevice(Long userId, DeviceRegisterRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDevice device = deviceRepo.findByDeviceToken(request.deviceToken())
                .orElse(new UserDevice());

        device.setUser(user);
        device.setDeviceToken(request.deviceToken());
        device.setPlatform(request.platform());
        device.setDeviceId(request.deviceId());
        device.setAppVersion(request.appVersion());
        device.setLastSeen(LocalDateTime.now());

        deviceRepo.save(device);
    }

    @Transactional
    public void unregisterDevice(Long userId, DeviceUnregisterRequest request) {
        deviceRepo.findByDeviceToken(request.deviceToken()).ifPresent(device -> {
            if (!device.getUser().getId().equals(userId)) {
                throw new RuntimeException("Token does not belong to this user");
            }
            deviceRepo.delete(device);
        });
    }
    public List<UserDevice> findByUser(Long userId) {
        return deviceRepo.findByUserId(userId);
    }
}

