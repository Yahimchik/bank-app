package com.example.bankapp.mapper;

import com.example.bankapp.dto.user.UserRequestDTO;
import com.example.bankapp.dto.user.UserResponseDTO;
import com.example.bankapp.entities.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        uses = {AccountMapper.class, EmailDataMapper.class, PhoneDataMapper.class})
public interface UserMapper {

    User convertToUser(UserRequestDTO userRequestDto);

    UserResponseDTO convertToUserResponse(User user);

    User convertToUserFromResponseDTO(UserResponseDTO user);
}

