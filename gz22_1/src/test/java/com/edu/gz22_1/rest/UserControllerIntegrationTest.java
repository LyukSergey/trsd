package com.edu.gz22_1.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

//@SpringBootTest - це анотація для інтеграційних тестів у Spring Boot. Вона:
//  - Запускає повний Spring ApplicationContext (всі bean'и, конфігурації)
//  - Імітує реальне середовище роботи додатку
// RANDOM_PORT - означає, що сервер буде запущений на випадковому порту, щоб уникнути конфліктів з іншими процесами
// DEFINED_PORT - означає, що сервер буде запущений на порту, визначеному в конфігурації (наприклад, application.properties)
// MOCK - означає, що сервер не буде запущений, а всі HTTP запити будуть оброблятися за допомогою моків (зазвичай використовується для тестування контролерів без запуску сервера)
// NONE - означає, що сервер не буде запущений і всі HTTP запити будуть оброблятися за допомогою моків, але без створення реального сервера
// (зазвичай використовується для тестування сервісів або репозиторіїв без контролерів)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @AutoConfigureMockMvc - це анотація для інтеграційних тестів у Spring Boot,
// яка автоматично налаштовує MockMvc для тестування веб-шару (контролерів)
// без запуску реального сервера.
// Вона дозволяє виконувати HTTP запити до контролерів і перевіряти їх відповіді,
// не піднімаючи повний сервер. Це корисно для швидкого тестування контролерів і їх взаємодії
// з іншими компонентами додатку.
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void testGetAllUsers() throws Exception {
        //Given - підготовка даних або налаштування середовища для тесту
        //When - виконання дії, яку ми хочемо протестувати
        //Then - перевірка результату, який ми очікуємо отримати
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("John Jones"));
    }

    @Test
    void testGetUserById() throws Exception {
        //Given - підготовка даних або налаштування середовища для тесту
        //When - виконання дії, яку ми хочемо протестувати
        //Then - перевірка результату, який ми очікуємо отримати
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(19));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        //Given - підготовка даних або налаштування середовища для тесту
        //When - виконання дії, яку ми хочемо протестувати
        //Then - перевірка результату, який ми очікуємо отримати
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("User with id 999 not found"));

    }

}