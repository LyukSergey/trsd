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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

//    @Test
//    void getAllUsers_ShouldReturnListOfUserDtos() {
//        // Given
//        final UserEntity user1 = new UserEntity(1L, "John", 30);
//        final UserEntity user2 = new UserEntity(2L, "Jane", 25);
//        final List<UserEntity> users = List.of(user1, user2);
//
//        when(userRepository.getAllUsers()).thenReturn(users);
//
//        // When
//        final List<UserDto> result = userService.getAllUsers();
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result).isNotEmpty();
//        assertThat(result.size()).isEqualTo(2);
//        assertThat(result.get(0)).hasNoNullFieldsOrProperties();
//        assertThat(result.get(0).getName()).isEqualTo("John");
//        assertThat(result.get(0).getAge()).isEqualTo(30);
//
//        assertThat(result.get(1)).hasNoNullFieldsOrProperties();
//        assertThat(result.get(1).getName()).isEqualTo("Jane");
//        assertThat(result.get(1).getAge()).isEqualTo(25);
//    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDtos() {
        // Given
        final UserEntity user1 = new UserEntity(1L, "John", 30);
        final UserEntity user2 = new UserEntity(2L, "Jane", 25);
        final List<UserEntity> users = List.of(user1, user2);

        when(userRepository.getAllUsers()).thenReturn(users);

        // When
        final List<UserDto> result = userService.getAllUsers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).hasNoNullFieldsOrProperties();
        assertThat(result.get(1)).hasNoNullFieldsOrProperties();
        assertThat(result.get(0).getName()).isEqualTo("John");
        assertThat(result.get(0).getAge()).isEqualTo(30);
        assertThat(result.get(1).getName()).isEqualTo("Jane");
        assertThat(result.get(1).getAge()).isEqualTo(25);
        verify(userRepository).getAllUsers();
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