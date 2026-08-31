package com.example.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.identity_service.dto.request.RoleRequest;
import com.example.identity_service.dto.response.RoleResponse;
import com.example.identity_service.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true) // Ignore
    Role toRole(RoleRequest request); // Chuyển đổi từ RoleRequest sang Role

    RoleResponse toRoleResponse(Role role); // Chuyển đổi từ Role sang RoleResponse
}
