package com.edu.l31.service;

import com.edu.l31.dto.RoleDto;
import java.util.List;

public interface RoleService {

    List<RoleDto> getAllRoles();

    RoleDto getRoleById(Long id);

    List<RoleDto> getRolesByUserId(Long userId);
}
