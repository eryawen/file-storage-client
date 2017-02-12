package lab.request;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lab.model.RemotePath;
import lab.utils.RequestURI;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Move implements Request {
	private RemotePath remotePath;
	private String newPath;
	private String session;
	
	@Override
	public Object doRequest() throws UnirestException {
		HttpResponse<JsonNode> jsonResponse =
			   Unirest.put(uri(RequestURI.MOVE, remotePath.getFileType()))
					.routeParam("path", remotePath.getEncodedPath())
					.queryString("newPath", newPath)
					.header("Cookie", session)
					.asJson();
		return jsonResponse;
	}
}
