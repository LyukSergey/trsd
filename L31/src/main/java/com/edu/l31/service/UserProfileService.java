package com.edu.l31.service;

import com.edu.l31.dto.UserProfileDto;

public interface UserProfileService {

    UserProfileDto getProfileByUserId(Long userId);
}
