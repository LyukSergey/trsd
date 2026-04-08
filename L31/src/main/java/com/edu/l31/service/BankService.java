package com.edu.l31.service;

import com.edu.l31.dto.BankDto;
import java.util.List;

public interface BankService {

    List<BankDto> getAllBanks();

    BankDto getBankById(Long id);

    BankDto getBankByUserId(Long userId);
}
