package com.edu.gz22_1.service;

import com.edu.gz22_1.dto.UserDto;
import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();
//
    UserDto getUserById(Long id);
}
