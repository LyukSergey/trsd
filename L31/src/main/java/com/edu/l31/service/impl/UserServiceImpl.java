package com.edu.l31.service.impl;

import com.edu.l31.dto.UserDto;
import com.edu.l31.entity.User;
import com.edu.l31.mapper.UserMapper;
import com.edu.l31.repository.UserRepository;
import com.edu.l31.service.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        final List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }
}
