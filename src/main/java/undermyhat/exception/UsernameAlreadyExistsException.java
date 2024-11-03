package undermyhat.exception;

public class UsernameAlreadyExistsException extends UserConflictException {
    public UsernameAlreadyExistsException(String username) {
        super("Username already used: " + username);
    }
}
