package com.edu.gz22_1.service;

import com.edu.gz22_1.dto.UserDto;
import com.edu.gz22_1.entity.UserEntity;
import com.edu.gz22_1.exception.ResourceNotFoundException;
import com.edu.gz22_1.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long id) {
       return userRepository.getUserById(id).map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %s not found", id)));
    }

    private UserDto convertToDto(UserEntity userEntity) {
        return new UserDto(userEntity.getName(), userEntity.getAge());
    }
}
