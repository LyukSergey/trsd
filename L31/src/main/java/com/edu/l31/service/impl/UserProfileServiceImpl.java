package com.edu.l31.service.impl;

import com.edu.l31.dto.UserProfileDto;
import com.edu.l31.mapper.UserProfileMapper;
import com.edu.l31.repository.UserProfileRepository;
import com.edu.l31.service.UserProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    @Transactional
    public UserProfileDto getProfileByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId)
                .map(userProfileMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Profile not found for user id: " + userId));
    }
}
