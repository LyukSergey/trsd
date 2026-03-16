package com.edu.gz22_1.service;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.edu.gz22_1.dto.UserDto;
import com.edu.gz22_1.entity.UserEntity;
import com.edu.gz22_1.exception.ResourceNotFoundException;
import com.edu.gz22_1.exception.UserException;
import com.edu.gz22_1.repository.UserRepository;
import java.util.List;
import java.util.Random;
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

    @Override
    public UserDto createUser(UserDto userDto) {
        final UserEntity userEntity = convertToEntityDto(userDto);
        return userRepository.save(userEntity)
                .map(this::convertToDto)
                .orElseThrow(() -> new UserException("Failed to create user"));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %s not found", id)));
        userRepository.deleteUserById(id);
    }

    private UserDto convertToDto(UserEntity userEntity) {
        return new UserDto(userEntity.getName(), userEntity.getAge());
    }

    private UserEntity convertToEntityDto(UserDto userDto) {
        return UserEntity.builder()
                .id(new Random().nextLong(1000))
                .name(userDto.getName())
                .age(userDto.getAge())
                .build();
    }
}
