package com.example.pruebaTecnica.controller;

import com.example.pruebaTecnica.dto.GradeDto;
import com.example.pruebaTecnica.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para operaciones CRUD de calificaciones
 * Aplica patrón MVC - Controller para manejar requests HTTP de calificaciones
 * académicas
 */
@RestController
@RequestMapping("/api/grades")
@Tag(name = "Calificaciones", description = "Endpoints para gestión de calificaciones académicas")
@SecurityRequirement(name = "bearerAuth")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    /**
     * Registrar una nueva calificación
     */
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Registrar calificación", description = "Registra una nueva calificación para un estudiante (solo profesores)")
    public ResponseEntity<GradeDto> createGrade(@Valid @RequestBody GradeDto gradeDto) {
        GradeDto createdGrade = gradeService.createGrade(gradeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGrade);
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Listar calificaciones", description = "Obtiene todas las calificaciones del estudiante autenticado (solo estudiantes)")
    public ResponseEntity<List<GradeDto>> getAllGrades() {
        List<GradeDto> grades = gradeService.getUserGrades();
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
    @Operation(summary = "Obtener calificación", description = "Obtiene una calificación específica por su ID (estudiantes solo las suyas)")
    public ResponseEntity<GradeDto> getGradeById(@PathVariable Long id) {
        GradeDto grade = gradeService.getGradeById(id);
        return ResponseEntity.ok(grade);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Actualizar calificación", description = "Actualiza una calificación existente (solo profesores)")
    public ResponseEntity<GradeDto> updateGrade(@PathVariable Long id, @Valid @RequestBody GradeDto gradeDto) {
        GradeDto updatedGrade = gradeService.updateGrade(id, gradeDto);
        return ResponseEntity.ok(updatedGrade);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Eliminar calificación", description = "Elimina una calificación por su ID (solo profesores)")
    public ResponseEntity<?> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.ok(new AuthController.MessageResponse("Calificación eliminada exitosamente"));
    }

}
