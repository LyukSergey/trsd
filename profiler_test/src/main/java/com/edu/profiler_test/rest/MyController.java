package com.edu.profiler_test.rest;

import com.edu.profiler_test.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/do")
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;

    @GetMapping
    public ResponseEntity<Void> getUsers() {
        myService.doWork();
        return ResponseEntity.noContent().build();
    }

}
