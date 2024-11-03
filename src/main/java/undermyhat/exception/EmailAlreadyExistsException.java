package undermyhat.exception;

public class EmailAlreadyExistsException extends UserConflictException {
    public EmailAlreadyExistsException(String email) {
        super("Email already used: " + email);
    }
}
