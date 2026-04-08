package com.edu.l31.mapper;

import com.edu.l31.dto.BankDto;
import com.edu.l31.entity.Bank;
import org.springframework.stereotype.Component;

@Component
public class BankMapper {

    public BankDto toDto(Bank bank) {
        if (bank == null) {
            return null;
        }

        BankDto dto = new BankDto();
        dto.setId(bank.getId());
        dto.setName(bank.getName());
        dto.setTotalAmount(bank.getTotalAmount());
        return dto;
    }
}
