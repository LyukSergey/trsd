package com.edu.l31.service.impl;

import com.edu.l31.dto.RoleDto;
import com.edu.l31.mapper.RoleMapper;
import com.edu.l31.repository.RoleRepository;
import com.edu.l31.service.RoleService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public RoleDto getRoleById(Long id) {
        return roleRepository.findById(id)
                .map(roleMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    @Override
    @Transactional
    public List<RoleDto> getRolesByUserId(Long userId) {
        return roleRepository.findByUsersId(userId).stream()
                .map(roleMapper::toDto)
                .toList();
    }
}
