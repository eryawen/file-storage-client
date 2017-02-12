package lab.exceptions;

public class UserAlreadyExistException extends BaseException {
	public UserAlreadyExistException() {
		super("User already exist");
	}
}
