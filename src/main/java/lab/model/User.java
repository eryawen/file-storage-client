package lab.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
	private final StringProperty user_name;
	private final StringProperty user_pass;
	
	public User(StringProperty user_name, StringProperty user_pass) {
		this.user_name = user_name;
		this.user_pass = user_pass;
	}
	
	public User() {
		this.user_name = new SimpleStringProperty("username");
		this.user_pass = new SimpleStringProperty("password");
	}
	
	public StringProperty user_passProperty() {
		return user_pass;
	}
	
	public StringProperty user_nameProperty() {
		return user_name;
	}
	
	public String getUser_name() {
		return user_name.get();
	}
	
	public String getUser_pass() {
		return user_pass.get();
	}
	
	public void setUser_name(String user_name) {
		this.user_name.set(user_name);
	}
	
	public void setUser_pass(String user_pass) {
		this.user_pass.set(user_pass);
	}
}
