package com.example.pruebaTecnica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pruebaTecnica.entity.Grade;
import com.example.pruebaTecnica.entity.User;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    
    // Obtener todas las calificaciones de un usuario ordenadas por fecha de creación
    List<Grade> findByUserOrderByCreatedAtDesc(User user);
    
    // Alternativa: obtener calificaciones usando el id del usuario
    @Query("SELECT g FROM Grade g WHERE g.user.id = :userId ORDER BY g.createdAt DESC")
    List<Grade> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    // Contar cuántas calificaciones tiene un usuario
    long countByUser(User user);
}
