package undermyhat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import undermyhat.infrastructure.model.UserEntity;
import undermyhat.services.JWTService;
import undermyhat.services.UserService;

import java.util.HashMap;
import java.util.Map;


@RestController
public class LoginController {


    private final JWTService jwtService;
    private final UserService userService;

    @Autowired
    public LoginController(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }
    Map<String, Object> errorResponse = new HashMap<>();

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginAndGetToken(@RequestBody String email, String password) {
        try {
            UserEntity userToLog = userService.getUserByEmail(email);

            if (userToLog == null) {
                throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email);
            }

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(password, userToLog.getPassword())) {
                errorResponse.put("error", "Mot de passe incorrect");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userToLog.getEmail(), userToLog.getPassword());
            String token = jwtService.generateToken(authentication);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", userToLog);
            System.out.println(ResponseEntity.ok(response));

            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException e) {
            System.err.println("Erreur lors de la connexion : " + e.getMessage());
            errorResponse.put("error", "Utilisateur non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion : " + e.getMessage());
            e.printStackTrace();
            errorResponse.put("error", "Erreur lors de la connexion de l'utilisateur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
}
