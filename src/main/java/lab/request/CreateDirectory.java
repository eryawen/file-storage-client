package lab.request;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lab.model.RemotePath;
import lab.utils.RequestURI;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateDirectory implements Request {
	private final RemotePath remotePath;
	private final String sessionHeader;
	
	@Override
	public Object doRequest() throws UnirestException {
		HttpResponse<JsonNode> jsonResponse =
			   Unirest.put(uri(RequestURI.CREATE_DIR, remotePath.getFileType()))
					.routeParam("path", remotePath.getEncodedPath())
					.header("Cookie", sessionHeader)
					.asJson();
		
		return jsonResponse;
	}
}
