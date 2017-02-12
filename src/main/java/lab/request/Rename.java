package lab.request;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lab.model.RemotePath;
import lab.utils.RequestURI;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Rename implements Request {
	private RemotePath remotePath;
	private String newName;
	private String session;
	
	@Override
	public Object doRequest() throws UnirestException {
		HttpResponse<JsonNode> jsonResponse =
			   Unirest.put(uri(RequestURI.RENAME, remotePath.getFileType()))
					.header("Cookie", session)
					.routeParam("path", remotePath.getEncodedPath())
					.queryString("newName", newName)
					.asJson();
		return jsonResponse;
	}
}
