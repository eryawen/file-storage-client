package lab.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lab.App;
import lab.exceptions.EmptyFieldException;
import lab.exceptions.UnsuccessfulLoginException;
import lab.model.User;
import lab.request.Login;

import static lab.utils.Status.UNSUCCESSFUL_LOGIN;
import static org.valid4j.Validation.otherwiseThrowing;
import static org.valid4j.Validation.validate;

public class LoginController {
	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordTextField;
	
	private User user = new User();
	private String sessionHeader = "";
	private App mainApp;
	
	public LoginController() {
	}
	
	@FXML
	private void initialize() {
		usernameTextField.textProperty().bindBidirectional(user.user_nameProperty());
		passwordTextField.textProperty().bindBidirectional(user.user_passProperty());
	}
	
	@FXML
	private void handleLogin() {
		try {
			validate(!user.getUser_name().isEmpty(), otherwiseThrowing(EmptyFieldException.class));
			validate(!user.getUser_pass().isEmpty(), otherwiseThrowing(EmptyFieldException.class));
			HttpResponse response = (HttpResponse) new Login(user).doRequest();
			validate(
				   response.getStatus() != UNSUCCESSFUL_LOGIN,
				   otherwiseThrowing(UnsuccessfulLoginException.class));
			sessionHeader = response.getHeaders().getFirst("Set-Cookie");
			mainApp.showMainLayout(sessionHeader, user);
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	private void handleException(Exception e) {
		user.setUser_name("");
		user.setUser_pass("");
		if (e instanceof EmptyFieldException) {
			showAlert("Fields cannot be empty");
		} else if (e instanceof UnsuccessfulLoginException) {
			showAlert("Wrong username or password");
		} else {
			showAlert("Something went wrong");
		}
	}
	
	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	@FXML
	private void handleCreateAccount() throws UnirestException {
		mainApp.showCreateAccountLayout();
	}
	
	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
	}
}
