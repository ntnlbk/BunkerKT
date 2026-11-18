package com.ntnlbk.presentation.dto.mapper

import com.ntnlbk.domain.model.User
import com.ntnlbk.domain.model.UserRole
import com.ntnlbk.presentation.dto.model.UserDTO
import com.ntnlbk.presentation.dto.model.UserRoleDTO

class UserDTOMapper {
    fun mapUserToUserDTO(user: User): UserDTO {
        return UserDTO(
            nickname = user.nickname,
            role = UserRoleDTO.valueOf(user.role.name)
        )
    }

    fun mapUserDTOToUser(userDTO: UserDTO): User {
        return User(
            nickname = userDTO.nickname,
            role = UserRole.valueOf(userDTO.role.name)
        )
    }
}