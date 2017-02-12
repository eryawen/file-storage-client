package lab.controller;

import com.mashape.unirest.http.HttpResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lab.App;
import lab.exceptions.EmptyFieldException;
import lab.exceptions.UserAlreadyExistException;
import lab.model.User;
import lab.request.CreateUser;
import lab.request.Login;

import static lab.utils.Status.USER_ALREADY_EXIST;
import static org.valid4j.Validation.otherwiseThrowing;
import static org.valid4j.Validation.validate;

public class CreateAccountController {
	@FXML
	private TextField usernameTextField;
	@FXML
	private TextField passwordTextField;
	
	private User user = new User();
	private String sessionHeader = "";
	private App mainApp;
	
	public CreateAccountController() {
	}
	
	@FXML
	private void initialize() {
		usernameTextField.textProperty().bindBidirectional(user.user_nameProperty());
		passwordTextField.textProperty().bindBidirectional(user.user_passProperty());
	}
	
	@FXML
	private void handleLogin() {
		mainApp.showLoginLayout();
	}
	
	@FXML
	private void handleCreateNewAccount() {
		try {
			validate(!user.getUser_name().isEmpty(), otherwiseThrowing(EmptyFieldException.class));
			validate(!user.getUser_pass().isEmpty(), otherwiseThrowing(EmptyFieldException.class));
			
			HttpResponse response = (HttpResponse) new CreateUser(user).doRequest();
			validate(
				   response.getStatus() != USER_ALREADY_EXIST,
				   otherwiseThrowing(UserAlreadyExistException.class));
			
			response = (HttpResponse) new Login(user).doRequest();
			sessionHeader = response.getHeaders().getFirst("Set-Cookie");
			mainApp.showMainLayout(sessionHeader, user);
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
	}
	
	private void handleException(Exception e) {
		user.setUser_name("");
		user.setUser_pass("");
		if (e instanceof EmptyFieldException) {
			showAlert("Fields cannot be empty");
		} else if (e instanceof UserAlreadyExistException) {
			showAlert("User already exists");
		} else {
			showAlert("Something went wrong");
		}
	}
	
	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
