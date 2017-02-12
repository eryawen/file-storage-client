package lab.request;

import com.mashape.unirest.http.exceptions.UnirestException;
import lab.model.RemotePath;

public interface Request {
	String BASE_URI = "http://localhost:4567";

	Object doRequest() throws UnirestException;
	
	default String uri(String requestURI, RemotePath.FileType fileType) {
		
		return (fileType == RemotePath.FileType.FILE)
			   ? String.format("%s/files%s", BASE_URI, requestURI)
			   : String.format("%s/folders%s", BASE_URI, requestURI);
	}
	
	default String uri(String requestURI) {
		return String.format("%s%s", BASE_URI, requestURI);
	}
}

