package com.example.bankapp.mapper;

import com.example.bankapp.dto.phone.PhoneDataRequestDTO;
import com.example.bankapp.dto.phone.PhoneDataResponseDTO;
import com.example.bankapp.entities.PhoneData;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true))
public interface PhoneDataMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    PhoneData convertToPhoneData(PhoneDataRequestDTO phoneDataRequestDTO);

    PhoneDataResponseDTO convertToPhoneDataResponseDTO(PhoneData phoneData);
}
