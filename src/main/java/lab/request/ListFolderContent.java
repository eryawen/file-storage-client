package lab.request;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lab.model.RemotePath;
import lab.utils.RequestURI;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListFolderContent implements Request {
	private RemotePath remotePath;
	private String session;
	private boolean recursive;
	
	@Override
	public Object doRequest() throws UnirestException {
		HttpResponse<JsonNode> jsonResponse =
			   Unirest.get(uri(RequestURI.LIST_FOLDER_CONTENT, remotePath.getFileType()))
					.routeParam("path", remotePath.getEncodedPath())
					.queryString("recursive", recursive)
					.header("Cookie", session)
					.asJson();
		return jsonResponse;
	}
}
