package undermyhat.services;

import org.springframework.stereotype.Service;
import undermyhat.exception.EmailAlreadyExistsException;
import undermyhat.exception.UsernameAlreadyExistsException;
import undermyhat.infrastructure.model.UserEntity;
import undermyhat.infrastructure.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createUser(UserEntity user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserByEmail(String email) {
        return  userRepository.findByEmail(email);
    }
}