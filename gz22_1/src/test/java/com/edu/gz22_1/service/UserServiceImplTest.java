package com.edu.gz22_1.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.edu.gz22_1.dto.UserDto;
import com.edu.gz22_1.entity.UserEntity;
import com.edu.gz22_1.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void getAllUsers_ShouldReturnListOfUserDtos() {
        // Given
        // Mock the userRepository to return a list of UserEntity objects
        final UserEntity userEntity1 = new UserEntity(1L, "John", 30);
        final UserEntity userEntity2 = new UserEntity(2L, "Jane", 25);
        when(userRepository.getAllUsers()).thenReturn(List.of(userEntity1, userEntity2));

        // When
        final List<UserDto> allUsers = userService.getAllUsers();

        // Then
        verify(userRepository).getAllUsers();
        assertThat(allUsers).isNotNull();
        assertThat(allUsers).hasSize(2);
        assertThat(allUsers.get(0)).hasNoNullFieldsOrProperties();
        assertThat(allUsers.get(0).getName()).isEqualTo(userEntity1.getName());
        assertThat(allUsers.get(0).getAge()).isEqualTo(userEntity1.getAge());
        assertThat(allUsers.get(1)).hasNoNullFieldsOrProperties();
        assertThat(allUsers.get(1).getName()).isEqualTo(userEntity2.getName());
        assertThat(allUsers.get(1).getAge()).isEqualTo(userEntity2.getAge());
    }

    @Test
    void getUserById_ShouldReturnUserDto() {
        // Given
        Long userId = 1L;
        // Mock the userRepository to return an Optional of UserEntity
        // when getUserById is called with the given userId
        final UserEntity userEntity = new UserEntity(1L, "John", 30);
        when(userRepository.getUserById(userId)).thenReturn(Optional.of(userEntity));

        // When
        final UserDto result = userService.getUserById(userId);

        // Then
        verify(userRepository).getUserById(userId);
        assertThat(result).isNotNull();
        assertThat(result).hasNoNullFieldsOrProperties();
        assertThat(result.getName()).isEqualTo(userEntity.getName());
        assertThat(result.getAge()).isEqualTo(userEntity.getAge());
    }

}