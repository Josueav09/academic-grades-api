package com.example.pruebaTecnica.service;

import com.example.pruebaTecnica.dto.GradeDto;
import com.example.pruebaTecnica.entity.Grade;
import com.example.pruebaTecnica.entity.User;
import com.example.pruebaTecnica.exception.ResourceNotFoundException;
import com.example.pruebaTecnica.repository.GradeRepository;
import com.example.pruebaTecnica.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de calificaciones académicas
 * Implementa la lógica de negocio para las operaciones CRUD de calificaciones
 */
@Service
@Transactional
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Registra una nueva calificación para un estudiante indicado
     * (solo profesores pueden usar este método)
     */
    public GradeDto createGrade(GradeDto gradeDto) {
        // Buscar estudiante por username
        User student = userRepository.findByUsername(gradeDto.getStudentUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estudiante no encontrado: " + gradeDto.getStudentUsername()));

        // Crear la calificación
        Grade grade = new Grade(
                gradeDto.getCourse(),
                gradeDto.getComments(),
                gradeDto.getScore(),
                student
        );

        Grade savedGrade = gradeRepository.save(grade);
        return convertToDto(savedGrade);
    }

    /**
     * Obtiene todas las calificaciones del estudiante autenticado
     */
    @Transactional(readOnly = true)
    public List<GradeDto> getUserGrades() {
        User currentUser = getCurrentUser();

        List<Grade> grades = gradeRepository.findByUserOrderByCreatedAtDesc(currentUser);

        return grades.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una calificación específica por ID
     */
    @Transactional(readOnly = true)
    public GradeDto getGradeById(Long gradeId) {
        User currentUser = getCurrentUser();

        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Calificación no encontrada con ID: " + gradeId));

        // Solo el dueño puede ver su calificación (si es estudiante)
        if (!grade.getUser().getId().equals(currentUser.getId())
            && !currentUser.getRole().equals("TEACHER")) {
            throw new ResourceNotFoundException(
                    "Calificación no encontrada con ID: " + gradeId);
        }

        return convertToDto(grade);
    }

    /**
     * Actualiza una calificación (solo profesores)
     */
    public GradeDto updateGrade(Long gradeId, GradeDto gradeDto) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Calificación no encontrada con ID: " + gradeId));

        // Actualizar campos
        grade.setCourse(gradeDto.getCourse());
        grade.setScore(gradeDto.getScore());
        grade.setComments(gradeDto.getComments());

        // Si es profesor y envía un estudiante, actualizar el dueño
        if (gradeDto.getStudentUsername() != null) {
            User student = userRepository.findByUsername(gradeDto.getStudentUsername())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Estudiante no encontrado: " + gradeDto.getStudentUsername()));
            grade.setUser(student);
        }

        Grade updatedGrade = gradeRepository.save(grade);
        return convertToDto(updatedGrade);
    }

    /**
     * Elimina una calificación (solo profesores)
     */
    public void deleteGrade(Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Calificación no encontrada con ID: " + gradeId));

        gradeRepository.delete(grade);
    }

    /**
     * Obtiene el estudiante autenticado actualmente
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
    }

    /**
     * Convierte una entidad Grade a GradeDto
     */
    private GradeDto convertToDto(Grade grade) {
        GradeDto dto = new GradeDto();
        dto.setId(grade.getId());
        dto.setCourse(grade.getCourse());
        dto.setScore(grade.getScore());
        dto.setComments(grade.getComments());
        dto.setStudentUsername(grade.getUser().getUsername()); // asignamos username del estudiante
        dto.setCreatedAt(grade.getCreatedAt());
        dto.setUpdatedAt(grade.getUpdatedAt());
        return dto;
    }
}
