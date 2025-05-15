package com.example.bankapp.mapper;

import com.example.bankapp.dto.account.AccountRequestDTO;
import com.example.bankapp.dto.account.AccountResponseDTO;
import com.example.bankapp.entities.Account;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true))
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "initialBalance", ignore = true)
    Account convertToAccount(AccountRequestDTO accountRequestDTO);

    AccountResponseDTO convertToAccountResponseDTO(Account account);
}
