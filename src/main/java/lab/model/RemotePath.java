package lab.model;

import lab.exceptions.EncodingProblemException;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
public class RemotePath {
	private String path;
	private FileType fileType;
	
	public String getEncodedPath() {
		try {
			return URLEncoder.encode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new EncodingProblemException();
		}
	}
	
	public RemotePath(String path, FileType fileType) {
		this.path = path;
		this.fileType = fileType;
	}
	
	public enum FileType {
		FILE,
		DIR,
	}
	
	@Override
	public String toString() {
		return getPath();
	}
}