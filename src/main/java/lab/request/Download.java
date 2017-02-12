package lab.request;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lab.exceptions.FileOperationException;
import lab.exceptions.InvalidPathParametherException;
import lab.model.RemotePath;
import lab.utils.RequestURI;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.valid4j.Validation.otherwiseThrowing;
import static org.valid4j.Validation.validate;

//import com.google.common.io.Files;

@AllArgsConstructor
public class Download implements Request {
	private Path local;
	private final RemotePath remotePath;
	private final String session;
	
	@Override
	public Object doRequest() throws UnirestException {
		validate(
			   remotePath.getFileType() == RemotePath.FileType.FILE,
			   otherwiseThrowing(InvalidPathParametherException.class));
		
		HttpResponse<InputStream> binaryResponse =
			   Unirest.get(uri(RequestURI.DOWNLOAD_FILE, RemotePath.FileType.FILE))
					.routeParam("path", remotePath.getEncodedPath())
					.header("Cookie", session)
					.asBinary();
		try {
//			Files.write(file, lines, Charset.forName("UTF-8"));
//			Charset utf8 = StandardCharsets.UTF_8;
//			Files.write(local, data);
			Files.copy(binaryResponse.getBody(), local);
		} catch (IOException e) {
			throw new FileOperationException();
		}
		return binaryResponse;
	}
}

