package com.example.pruebaTecnica.controller;

import com.example.pruebaTecnica.dto.GradeDto;
import com.example.pruebaTecnica.dto.UserLoginDto;
import com.example.pruebaTecnica.dto.UserRegistrationDto;
import com.example.pruebaTecnica.repository.GradeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para GradeController (Teacher crea, Student consulta)
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class GradeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GradeRepository gradeRepository;

    private String teacherToken;
    private String studentToken;

    @BeforeEach
    void setUp() throws Exception {
        gradeRepository.deleteAll();

        // registrar TEACHER
        UserRegistrationDto teacherDto = new UserRegistrationDto();
        teacherDto.setUsername("teacher1");
        teacherDto.setEmail("teacher1@example.com");
        teacherDto.setPassword("password123");
        teacherDto.setRole("TEACHER");
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacherDto)));

        // registrar STUDENT
        UserRegistrationDto studentDto = new UserRegistrationDto();
        studentDto.setUsername("student1");
        studentDto.setEmail("student1@example.com");
        studentDto.setPassword("password123");
        studentDto.setRole("STUDENT");
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)));

        // login TEACHER
        UserLoginDto teacherLogin = new UserLoginDto("teacher1", "password123");
        MvcResult teacherLoginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacherLogin)))
                .andExpect(status().isOk())
                .andReturn();
        teacherToken = objectMapper.readTree(teacherLoginResult.getResponse().getContentAsString())
                .get("token").asText();

        // login STUDENT
        UserLoginDto studentLogin = new UserLoginDto("student1", "password123");
        MvcResult studentLoginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentLogin)))
                .andExpect(status().isOk())
                .andReturn();
        studentToken = objectMapper.readTree(studentLoginResult.getResponse().getContentAsString())
                .get("token").asText();
    }

    @Test
    void createGrade_AsTeacher_Success() throws Exception {
        GradeDto gradeDto = new GradeDto(null, "Matemáticas", 18.5, "Buen trabajo",
                "student1", LocalDateTime.now(), null);

        mockMvc.perform(post("/api/grades")
                .header("Authorization", "Bearer " + teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.course").value("Matemáticas"))
                .andExpect(jsonPath("$.score").value(18.5))
                .andExpect(jsonPath("$.studentUsername").value("student1"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void createGrade_AsStudent_Forbidden() throws Exception {
        GradeDto gradeDto = new GradeDto(null, "Historia", 14.0, "Regular",
                "student1", LocalDateTime.now(), null);

        mockMvc.perform(post("/api/grades")
                .header("Authorization", "Bearer " + studentToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDto)))
                .andExpect(status().isForbidden()); // estudiante no puede crear
    }

    @Test
    void getAllGrades_AsStudent_Success() throws Exception {
        // primero un TEACHER crea
        GradeDto gradeDto = new GradeDto(null, "Inglés", 12.0, "Falta estudiar",
                "student1", LocalDateTime.now(), null);

        mockMvc.perform(post("/api/grades")
                .header("Authorization", "Bearer " + teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDto)))
                .andExpect(status().isCreated());

        // ahora el STUDENT consulta
        mockMvc.perform(get("/api/grades")
                .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].course").value(hasItem("Inglés")));
    }

    @Test
    void createGrade_Unauthorized_WithoutToken() throws Exception {
        GradeDto gradeDto = new GradeDto(null, "Física", 10.0, "Debe mejorar",
                "student1", LocalDateTime.now(), null);

        mockMvc.perform(post("/api/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDto)))
                .andExpect(status().isUnauthorized());
    }
}
