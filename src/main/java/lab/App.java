package lab;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lab.controller.CreateAccountController;
import lab.controller.LoginController;
import lab.controller.MainController;
import lab.model.User;

import java.io.IOException;

public class App extends Application {
	LoginController loginController;
	CreateAccountController createAccountController;
	MainController mainController;
	
	private Stage primaryStage;
	private Scene loginScene;
	private Scene createScene;
	private Scene mainScene;
	
	public void showMainLayout(String sessionHeader, User user) {
		mainController.initView(sessionHeader, user);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}
	
	public void showLoginLayout() {
		primaryStage.setScene(loginScene);
		primaryStage.show();
	}
	
	public void showCreateAccountLayout() {
		primaryStage.setScene(createScene);
		primaryStage.show();
	}
	
	@Override
	public void start(Stage primaryStage) {
		initRequestSettings();
		initLayouts();
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("REST client");
		showLoginLayout();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void initRequestSettings() {
		Unirest.setObjectMapper(
			   new ObjectMapper() {
				   private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper =
						 new com.fasterxml.jackson.databind.ObjectMapper();
				   
				   public <T> T readValue(String value, Class<T> valueType) {
					   try {
						   return jacksonObjectMapper.readValue(value, valueType);
					   } catch (IOException e) {
						   throw new RuntimeException(e);
					   }
				   }
				   
				   public String writeValue(Object value) {
					   try {
						   return jacksonObjectMapper.writeValueAsString(value);
					   } catch (JsonProcessingException e) {
						   throw new RuntimeException(e);
					   }
				   }
			   });
	}
	
	private void initLayouts() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("/loginView.fxml"));
			AnchorPane loginLayout = loader.load();
			loginController = loader.getController();
			loginController.setMainApp(this);
			loginScene = new Scene(loginLayout);
			
			loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("/createAccountView.fxml"));
			AnchorPane createAccountLayout = loader.load();
			createAccountController = loader.getController();
			createAccountController.setMainApp(this);
			createScene = new Scene(createAccountLayout);
			
			loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("/mainView.fxml"));
			AnchorPane mainLayout = loader.load();
			
			mainController = loader.getController();
			mainController.setMainApp(this);
			mainScene = new Scene(mainLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
