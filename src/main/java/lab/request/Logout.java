package lab.request;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lab.utils.RequestURI;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Logout implements Request {
	private String session;
	
	@Override
	public Object doRequest() throws UnirestException {
		HttpResponse<String> response =
			   Unirest.get(uri(RequestURI.USER_LOGOUT)).header("Cookie", session).asString();
		return response;
	}
}
