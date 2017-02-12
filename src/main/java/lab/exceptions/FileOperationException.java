package lab.exceptions;

public class FileOperationException extends BaseException {
	public FileOperationException() {
		super("Something went wrong while reading/writing to file");
	}
}
