package undermyhat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import undermyhat.infrastructure.model.UserEntity;
import undermyhat.services.JWTService;
import undermyhat.services.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SignUpController {
    private final JWTService jwtService;
    private final UserService userService;

    @Autowired
    public SignUpController(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signUpAndGetToken(@RequestBody UserEntity user) {
        try {
            userService.createUser(user);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

            String token = jwtService.generateToken(authentication);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);
            System.out.println(ResponseEntity.ok(response));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'inscription : " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erreur lors de l'inscription de l'utilisateur : " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
