package com.edu.l31.controller;

import com.edu.l31.dto.BankDto;
import com.edu.l31.dto.UserDto;
import com.edu.l31.dto.UserRegistrationDto;
import com.edu.l31.service.BankManagementService;
import com.edu.l31.service.BankService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankManagementService bankManagementService;
    private final BankService bankService;

    @GetMapping
    public ResponseEntity<List<BankDto>> getAllBanks() {
        return ResponseEntity.ok(bankService.getAllBanks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankDto> getBankById(@PathVariable Long id) {
        return ResponseEntity.ok(bankService.getBankById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<BankDto> getBankByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(bankService.getBankByUserId(userId));
    }

    @PostMapping("/{bank_id}/users")
    public ResponseEntity<UserDto> registerUser(@PathVariable("bank_id") Long bankId, @RequestBody UserRegistrationDto request) {
        final UserDto userDto = bankManagementService.registerNewUser(request.getName(), request.getSurname(), bankId);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        bankManagementService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bankId}/users")
    public ResponseEntity<List<UserDto>> getUsersByBank(@PathVariable Long bankId) {
        return ResponseEntity.ok(bankManagementService.getUsersByBank(bankId));
    }

    @GetMapping("/{bank-id}/users/max-surname-length")
    public ResponseEntity<UserDto> getMaxSurnameLength(@PathVariable(name = "bank-id") Long bankId,
            @RequestParam(value = "with-stream", required = false, defaultValue = "false") Boolean withStream) {
        return ResponseEntity.ok(bankManagementService.getMaxSurnameLength(bankId, withStream));
    }
}