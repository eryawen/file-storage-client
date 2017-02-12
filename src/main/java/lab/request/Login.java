package lab.request;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lab.model.User;
import lab.utils.RequestURI;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Login implements Request {
	private User user;
	
	@Override
	public Object doRequest() throws UnirestException {
		HttpResponse<String> response =
			   Unirest.get(uri(RequestURI.USER_LOGIN))
					.basicAuth(user.getUser_name(), user.getUser_pass())
					.asString();
		return response;
	}
}
