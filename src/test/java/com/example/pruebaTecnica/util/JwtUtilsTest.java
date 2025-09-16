// package com.example.pruebaTecnica.util;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.when;

// import java.util.ArrayList;
// import java.util.Collection;
// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.test.util.ReflectionTestUtils;

// @ExtendWith(MockitoExtension.class)
// class JwtUtilsTest {

//     @InjectMocks
//     private JwtUtils jwtUtils;

//     @Mock
//     private Authentication authentication;

//     @Mock
//     private UserDetails userDetails;

//     @BeforeEach
//     void setUp() {
//         // Configurar propiedades JWT
//         ReflectionTestUtils.setField(jwtUtils, "jwtSecret",
//                 "mySecretKey123456789012345678901234567890123456789012345678901234567890");
//         ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 86400000);

//         // Colección de roles
//         List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));

//         // Configurar mocks con cast explícito
//         when(userDetails.getUsername()).thenReturn("testuser");
//         @SuppressWarnings("unchecked")
//         Collection<? extends GrantedAuthority> castAuthorities = (Collection<? extends GrantedAuthority>) authorities;
//         when(userDetails.getAuthorities()).thenReturn(castAuthorities);
//         when(authentication.getPrincipal()).thenReturn(userDetails);
//     }

//     @Test
//     void generateJwtToken_ShouldReturnNonEmptyToken() {
//         String token = jwtUtils.generateJwtToken(authentication);
//         assertNotNull(token);
//         assertFalse(token.isEmpty());
//         assertTrue(token.contains("."));
//     }

//     @Test
//     void validateJwtToken_ShouldReturnTrueForValidToken() {
//         String token = jwtUtils.generateJwtToken(authentication);
//         assertTrue(jwtUtils.validateJwtToken(token));
//     }

//     @Test
//     void validateJwtToken_ShouldReturnFalseForInvalidToken() {
//         String invalidToken = "invalid.token.here";
//         assertFalse(jwtUtils.validateJwtToken(invalidToken));
//     }

//     @Test
//     void getUserNameFromJwtToken_ShouldReturnCorrectUsername() {
//         String token = jwtUtils.generateJwtToken(authentication);
//         String username = jwtUtils.getUserNameFromJwtToken(token);
//         assertEquals("testuser", username);
//     }

//     @Test
//     void getRolesFromJwtToken_ShouldReturnCorrectRoles() {
//         String token = jwtUtils.generateJwtToken(authentication);
//         List<String> roles = jwtUtils.getRolesFromJwtToken(token);
//         assertNotNull(roles);
//         assertEquals(1, roles.size());
//         assertEquals("ROLE_STUDENT", roles.get(0));
//     }
// }
