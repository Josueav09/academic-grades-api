package com.example.pruebaTecnica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pruebaTecnica.entity.User;

import java.util.Optional;

/**
 * Repositorio para la entidad User
 * Aplica patrón Repository para abstraer el acceso a datos
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Busca un usuario por su nombre de usuario
     * @param username nombre de usuario
     * @return Optional con el usuario encontrado
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Busca un usuario por su email
     * @param email email del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Verifica si existe un usuario con el nombre de usuario dado
     * @param username nombre de usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica si existe un usuario con el email dado
     * @param email email del usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
}