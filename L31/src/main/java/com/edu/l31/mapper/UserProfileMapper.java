package com.edu.l31.mapper;

import com.edu.l31.dto.UserProfileDto;
import com.edu.l31.entity.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfileDto toDto(UserProfile profile) {
        if (profile == null) {
            return null;
        }

        UserProfileDto dto = new UserProfileDto();
        dto.setId(profile.getId());
        dto.setEmail(profile.getEmail());
        dto.setPhone(profile.getPhone());
        dto.setUserId(profile.getUser().getId());
        return dto;
    }
}
