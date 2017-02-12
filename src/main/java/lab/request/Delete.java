package lab.request;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lab.model.RemotePath;
import lab.utils.RequestURI;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Delete implements Request {
	private final RemotePath remotePath;
	private final String session;
	
	@Override
	public Object doRequest() throws UnirestException {
		HttpResponse<JsonNode> jsonResponse =
			   Unirest.delete(uri(RequestURI.DELETE, remotePath.getFileType()))
					.routeParam("path", remotePath.getEncodedPath())
					.header("Cookie", session)
					.asJson();
		return jsonResponse;
	}
}
