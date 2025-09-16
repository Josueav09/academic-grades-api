package com.example.pruebaTecnica.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO para las notas académicas (grades)
 * Encapsula los datos de la nota sin exponer detalles de implementación
 */
public class GradeDto {

    private Long id;

    @NotBlank(message = "El curso es obligatorio")
    @Size(max = 100)
    private String course;

    @Min(0)
    @Max(20)
    private Double score;

    @Size(max = 500)
    private String comments;

    @NotBlank(message = "El estudiante es obligatorio")
    private String studentUsername; // <-- aquí

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public GradeDto() {}

    // Constructor completo
    public GradeDto(Long id, String course, Double score, String comments, String studentUsername,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.course = course;
        this.score = score;
        this.comments = comments;
        this.studentUsername = studentUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getStudentUsername() {
        return studentUsername;
    }
    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }
}
