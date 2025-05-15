package com.example.bankapp.mapper;

import com.example.bankapp.dto.email.EmailDataRequestDTO;
import com.example.bankapp.dto.email.EmailDataResponseDTO;
import com.example.bankapp.entities.EmailData;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true))
public interface EmailDataMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    EmailData convertToEmailData(EmailDataRequestDTO emailDataRequestDTO);

    EmailDataResponseDTO convertToEmailDataResponseDTO(EmailData emailData);
}
