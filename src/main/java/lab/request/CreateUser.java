package lab.request;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lab.model.User;
import lab.utils.RequestURI;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateUser implements Request {
	private final User user;
	
	@Override
	public Object doRequest() throws UnirestException {
		HttpResponse<JsonNode> jsonResponse =
			   Unirest.post(uri(RequestURI.CREATE_USER)).body(user).asJson();
		return jsonResponse;
	}
}
