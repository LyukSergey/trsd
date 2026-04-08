package com.edu.l31.service.impl;

import com.edu.l31.dto.BankDto;
import com.edu.l31.mapper.BankMapper;
import com.edu.l31.repository.BankRepository;
import com.edu.l31.service.BankService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    @Override
    @Transactional
    public List<BankDto> getAllBanks() {
        return bankRepository.findAll().stream()
                .map(bankMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BankDto getBankById(Long id) {
        return bankRepository.findById(id)
                .map(bankMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Bank not found with id: " + id));
    }

    @Override
    @Transactional
    public BankDto getBankByUserId(Long userId) {
        return bankRepository.findByUsersId(userId)
                .map(bankMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Bank not found for user id: " + userId));
    }
}
