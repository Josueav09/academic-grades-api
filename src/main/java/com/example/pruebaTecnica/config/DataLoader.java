package com.example.pruebaTecnica.config;

import com.example.pruebaTecnica.entity.Grade;
import com.example.pruebaTecnica.entity.User;
import com.example.pruebaTecnica.repository.GradeRepository;
import com.example.pruebaTecnica.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Carga datos de prueba en la base de datos al iniciar la aplicación
 * Aplica patrón Command para ejecutar la inicialización de datos
 */
@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            loadTestData();
        }
    }

    private void loadTestData() {
        logger.info("Cargando datos de prueba...");

        // Crear usuarios de prueba con roles
        // Crear usuarios de prueba con roles
        User teacher = new User("teacher1", "teacher1@example.com", passwordEncoder.encode("password123"), "TEACHER");
        User student = new User("student1", "student1@example.com", passwordEncoder.encode("password123"), "STUDENT");

        userRepository.save(teacher);
        userRepository.save(student);

        // Crear calificaciones de prueba para el estudiante
        Grade grade1 = new Grade("Matemáticas", "Examen Parcial", 14.0, student);
        Grade grade2 = new Grade("Programación", "Proyecto Final", 15.5, student);

        gradeRepository.save(grade1);
        gradeRepository.save(grade2);

        logger.info("Datos de prueba cargados exitosamente");
        logger.info("Usuario profesor: teacher1 / password123");
        logger.info("Usuario estudiante: student1 / password123");
    }
}
