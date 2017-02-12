package lab.exceptions;

public class EmptyFieldException extends RuntimeException {
	public EmptyFieldException() {
		super("Fields cannot be empty");
	}
}
