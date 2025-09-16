package com.example.pruebaTecnica.service;

import com.example.pruebaTecnica.dto.GradeDto;
import com.example.pruebaTecnica.entity.Grade;
import com.example.pruebaTecnica.entity.User;
import com.example.pruebaTecnica.exception.ResourceNotFoundException;
import com.example.pruebaTecnica.repository.GradeRepository;
import com.example.pruebaTecnica.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para GradeService adaptadas al contexto actual
 */
@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private GradeService gradeService;

    private User testUser;
    private Grade testGrade;
    private GradeDto testGradeDto;

    @BeforeEach
    void setUp() {
        testUser = new User("student1", "student@example.com", "password", "STUDENT");
        testUser.setId(1L);

        testGrade = new Grade("Math", "Grade description", 18.0, testUser);
        testGrade.setId(1L);
        testGrade.setCreatedAt(LocalDateTime.now());
        testGrade.setUpdatedAt(LocalDateTime.now());

        testGradeDto = new GradeDto();
        testGradeDto.setCourse("Math");
        testGradeDto.setComments("Grade description");
        testGradeDto.setScore(18.0);
        testGradeDto.setStudentUsername("student1");

        mockSecurityContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void mockSecurityContext() {
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn("student1");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createGrade_Success() {
        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(testUser));
        when(gradeRepository.save(any(Grade.class))).thenReturn(testGrade);

        GradeDto result = gradeService.createGrade(testGradeDto);

        assertNotNull(result);
        assertEquals("Math", result.getCourse());
        assertEquals(18.0, result.getScore());
        assertEquals("Grade description", result.getComments());
        verify(gradeRepository, times(1)).save(any(Grade.class));
        verify(userRepository, times(1)).findByUsername("student1");
    }

    @Test
    void createGrade_ThrowsException_WhenUserNotFound() {
        when(userRepository.findByUsername("student1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeService.createGrade(testGradeDto));
        verify(gradeRepository, never()).save(any(Grade.class));
    }

    @Test
    void getUserGrades_Success() {
        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(testUser));
        when(gradeRepository.findByUserOrderByCreatedAtDesc(testUser))
                .thenReturn(Arrays.asList(testGrade));

        List<GradeDto> result = gradeService.getUserGrades();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Math", result.get(0).getCourse());
        assertEquals(18.0, result.get(0).getScore());
        verify(userRepository, times(1)).findByUsername("student1");
        verify(gradeRepository, times(1)).findByUserOrderByCreatedAtDesc(testUser);
    }

    @Test
    void getUserGrades_ThrowsException_WhenUserNotFound() {
        when(userRepository.findByUsername("student1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeService.getUserGrades());
        // ❌ no ponemos when(gradeRepository...) porque nunca se usa
        verify(gradeRepository, never()).findByUserOrderByCreatedAtDesc(any(User.class));
    }

    @Test
    void getGradeById_Success() {
        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(testUser));
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(testGrade));

        GradeDto result = gradeService.getGradeById(1L);

        assertNotNull(result);
        assertEquals("Math", result.getCourse());
        assertEquals(18.0, result.getScore());
        verify(gradeRepository, times(1)).findById(1L);
    }

    @Test
    void getGradeById_ThrowsException_WhenGradeNotFound() {
        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(testUser));
        when(gradeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeService.getGradeById(1L));
    }

    @Test
    void getGradeById_ThrowsException_WhenUserNotOwner() {
        User otherUser = new User("other", "other@example.com", "password", "STUDENT");
        otherUser.setId(2L);
        Grade otherGrade = new Grade("Physics", "Other grade", 15.0, otherUser);
        otherGrade.setId(1L);

        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(testUser));
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(otherGrade));

        assertThrows(ResourceNotFoundException.class, () -> gradeService.getGradeById(1L));
    }

    @Test
    void updateGrade_Success() {
        GradeDto updateDto = new GradeDto();
        updateDto.setCourse("Physics");
        updateDto.setComments("Updated description");
        updateDto.setScore(19.0);
        updateDto.setStudentUsername("student1");

        Grade updatedGrade = new Grade("Physics", "Updated description", 19.0, testUser);
        updatedGrade.setId(1L);

        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(testUser));
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(testGrade));
        when(gradeRepository.save(any(Grade.class))).thenReturn(updatedGrade);

        GradeDto result = gradeService.updateGrade(1L, updateDto);

        assertNotNull(result);
        assertEquals("Physics", result.getCourse());
        assertEquals(19.0, result.getScore());
        assertEquals("Updated description", result.getComments());
        verify(gradeRepository, times(1)).save(testGrade);
    }

    @Test
    void deleteGrade_Success() {
        lenient().when(userRepository.findByUsername("student1")).thenReturn(Optional.of(testUser));
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(testGrade));

        assertDoesNotThrow(() -> gradeService.deleteGrade(1L));
        verify(gradeRepository, times(1)).delete(testGrade);
    }

    @Test
    void deleteGrade_ThrowsException_WhenGradeNotFound() {
        lenient().when(userRepository.findByUsername("student1")).thenReturn(Optional.of(testUser));
        when(gradeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gradeService.deleteGrade(1L));
        verify(gradeRepository, never()).delete(any(Grade.class));
    }
}
