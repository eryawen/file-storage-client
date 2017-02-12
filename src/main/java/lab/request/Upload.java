package lab.request;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lab.exceptions.InvalidPathParametherException;
import lab.model.RemotePath;
import lab.utils.RequestURI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.valid4j.Validation.otherwiseThrowing;
import static org.valid4j.Validation.validate;

public class Upload implements Request {
	private byte[] fileContent;
	private String fileContent2;
	private final RemotePath remote;
	private final String session;
	private Path localPath;
	
	@Override
	public Object doRequest() throws UnirestException {
		File file = new File(localPath.toUri());
		validate(file.exists(), otherwiseThrowing(InvalidPathParametherException.class));
		validate(file.isFile(), otherwiseThrowing(InvalidPathParametherException.class));
		
		try {
			fileContent = Files.readAllBytes(localPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HttpResponse<JsonNode> binaryResponse =
			   Unirest.post(uri(RequestURI.UPLOAD_FILE, RemotePath.FileType.FILE))
					.queryString("path", remote.getEncodedPath())
					.header("Cookie", session)
					.body(fileContent)
					.asJson();
		return binaryResponse;
	}
	
	public Upload(Path path, RemotePath remote, String session) {
		this.remote = remote;
		this.session = session;
		this.localPath = path;
	}
}

