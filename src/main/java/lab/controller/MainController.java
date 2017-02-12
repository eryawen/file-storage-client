package lab.controller;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lab.App;
import lab.exceptions.BaseException;
import lab.exceptions.UnsuccessfulLoginException;
import lab.model.FileSystem;
import lab.model.FolderContent;
import lab.model.RemotePath;
import lab.model.User;
import lab.request.*;
import lab.utils.Status;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static lab.model.FileSystem.currentLocalFileProperty;
import static lab.model.RemotePath.FileType;
import static lab.utils.Status.*;
import static org.valid4j.Validation.otherwiseThrowing;
import static org.valid4j.Validation.validate;

public class MainController {
	final Gson gson = new Gson();
	private App app;
	private String sessionHeader;
	private User user;
	private FolderContent rootFolderContent;
	private FileSystem fileSystem;
	
	private StringProperty request = new SimpleStringProperty("Write request here");
	private StringProperty response = new SimpleStringProperty("Server response");
	
	@FXML
	private Label usernameLabel;
	@FXML
	private TextField requestTextField;
	@FXML
	private TextArea responseTextArea;
	@FXML
	private TreeView<File> localFileSystemTreeView;
	@FXML
	private TreeView<RemotePath> remoteFileSystemTreeView;
	@FXML
	private TextField currentRemoteFileTextField;
	@FXML
	private TextField currentLocalFileTextField;
	@FXML
	private Label possibleOperationsLabel;
	
	@FXML
	private void sendRequest() {
		try {
			Path local = Paths.get(currentLocalFileProperty.get());
			FileType fileType =
				   FileSystem.remoteFileSystemTree.getResourceType(
						 FileSystem.currentRemoteFileProperty.get());
			RemotePath remote = new RemotePath(FileSystem.currentRemoteFileProperty.get(), fileType);
			HttpResponse httpResponse = (HttpResponse) new TextRequestHandler(local, remote,
																 sessionHeader).sendRequest(
				   request.getValue());
			if (statusCodeIsSuccess(httpResponse)) {
				updateTreeViews();
			} else {
				showErrorStatus(httpResponse);
			}
			response.setValue(httpResponse.getBody().toString());
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	private void handleException(Exception e) {
		if (e instanceof BaseException) {
			showAlert(e.getMessage());
			return;
		} else if (e instanceof UnirestException) {
			showAlert("Problem with connection");
		} else e.printStackTrace();
	}
	
	private boolean statusCodeIsSuccess(HttpResponse httpResponse) {
		return httpResponse.getStatus() == SUCCESS_GET || httpResponse.getStatus() == SUCCESS_CREATED;
	}
	
	private void showErrorStatus(HttpResponse httpResponse) {
		if (httpResponse.getStatus() == Status.SESSION_EXPIRED) {
			logIn();
			sendRequest();
		}
		if (httpResponse.getStatus() == Status.INVALID_PATH
		    || httpResponse.getStatus() == Status.NONEXISTENT_FOLDER) {
			showAlert("Invalid path");
		}
		if (httpResponse.getStatus() == Status.WRONG_PARAM_FORMAT) {
			showAlert("Wrong query format");
		}
		if (httpResponse.getStatus() == Status.DATABASE_ACCESS_PROBLEM) {
			showAlert("Something went wrong while connecting to database");
		}
		if (httpResponse.getStatus() == Status.AUTHORISATION_REQUIRED) {
			showAlert("Authorisation required");
		}
	}
	
	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	@FXML
	private void handleLogout() {
		try {
			new Logout(sessionHeader).doRequest();
			app.showLoginLayout();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
	
	private void logIn() {
		try {
			HttpResponse response = (HttpResponse) new Login(user).doRequest();
			validate(response.getStatus() != Status.UNSUCCESSFUL_LOGIN,
				    otherwiseThrowing(UnsuccessfulLoginException.class));
			sessionHeader = response.getHeaders().getFirst("Set-Cookie");
			
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	@FXML
	private void handleMove_fromMenu() {
		changeRemotePathToSelectedInTreeView();
		
		TextInputDialog dialog = new TextInputDialog(FileSystem.currentRemoteFileProperty.get());
		dialog.setContentText("New path:");
		Optional<String> result = dialog.showAndWait();
		try {
			if (result.isPresent()) {
				FileType fileType =
					   FileSystem.remoteFileSystemTree.getResourceType(
							 FileSystem.currentRemoteFileProperty.get());
				RemotePath remote = new RemotePath(FileSystem.currentRemoteFileProperty.get(), fileType);
				HttpResponse httpResponse =
					   (HttpResponse) new Move(remote, result.get(), sessionHeader).doRequest();
				statusCodeIsSuccess(httpResponse);
				updateTreeViews();
			}
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	@FXML
	private void handleRename_fromMenu() {
		changeRemotePathToSelectedInTreeView();
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setContentText("New name:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			FileType fileType = FileSystem.remoteFileSystemTree.getResourceType(
				   FileSystem.currentRemoteFileProperty.get());
			RemotePath remote = new RemotePath(FileSystem.currentRemoteFileProperty.get(), fileType);
			try {
				HttpResponse httpResponse =
					   (HttpResponse) new Rename(remote, result.get(), sessionHeader).doRequest();
				statusCodeIsSuccess(httpResponse);
				updateTreeViews();
			} catch (Exception e) {
				handleException(e);
			}
		}
	}
	
	@FXML
	private void changeLocalPathToSelectedInTreeView() {
		currentLocalFileProperty.setValue(
			   localFileSystemTreeView
					 .getSelectionModel()
					 .selectedItemProperty()
					 .get()
					 .getValue()
					 .toString());
	}
	
	@FXML
	private void changeRemotePathToSelectedInTreeView() {
		FileSystem.currentRemoteFileProperty.setValue(
			   remoteFileSystemTreeView
					 .getSelectionModel()
					 .selectedItemProperty()
					 .get()
					 .getValue()
					 .toString());
	}
	
	private FolderContent getUpdatedRootContent() {
		try {
			HttpResponse httpResponse;
			do {
				httpResponse = (HttpResponse) new ListFolderContent(
					   new RemotePath(fileSystem.remoteRoot, FileType.DIR), sessionHeader, true).doRequest();
				if (httpResponse.getStatus() == SESSION_EXPIRED) {
					logIn();
				} else {
					break;
				}
			} while (true);
			return gson.fromJson(httpResponse.getBody().toString(), FolderContent.class);
		} catch (Exception e) {
			handleException(e);
			return rootFolderContent;
		}
	}
	
	@FXML
	private void showPossibleTextCommandsLabel() {
		possibleOperationsLabel.setVisible(true);
	}
	
	@FXML
	private void hidePossibleTextCommandsLabel() {
		possibleOperationsLabel.setVisible(false);
	}
	
	public void initView(String sessionHeader, User user) {
		this.sessionHeader = sessionHeader;
		this.user = user;
		this.fileSystem = new FileSystem(String.format("/%s", user.getUser_name()));
		
		request.setValue("Write request here");
		response.setValue("Server response");
		
		usernameLabel.textProperty().bind(user.user_nameProperty());
		request.bind(requestTextField.textProperty());
		responseTextArea.textProperty().bind(response);
		
		FileSystem.currentRemoteFileProperty.setValue(fileSystem.remoteRoot);
		currentLocalFileTextField.textProperty().bindBidirectional(currentLocalFileProperty);
		currentRemoteFileTextField
			   .textProperty()
			   .bindBidirectional(FileSystem.currentRemoteFileProperty);
		
		FileSystem.localFileSystem = SimpleFileTreeItem.getTreeView(FileSystem.LOCAL_ROOT);
		localFileSystemTreeView.rootProperty().bind(FileSystem.localFileSystem.rootProperty());
		
		rootFolderContent = getUpdatedRootContent();
		FileSystem.remoteFileSystemTree = new RemoteFileSystemTree(sessionHeader);
		FileSystem.remoteFileSystem =
			   FileSystem.remoteFileSystemTree.buildRemoteFileSystem(rootFolderContent);
		remoteFileSystemTreeView.rootProperty().bind(FileSystem.remoteFileSystem.rootProperty());
	}
	
	private void updateTreeViews() {
		remoteFileSystemTreeView.rootProperty().unbind();
		FileSystem.remoteFileSystem = FileSystem.remoteFileSystemTree.getUpdatedRemoteFileSystem(
			   getUpdatedRootContent());
		remoteFileSystemTreeView.rootProperty().bind(FileSystem.remoteFileSystem.rootProperty());
		
		localFileSystemTreeView.rootProperty().unbind();
		FileSystem.localFileSystem = SimpleFileTreeItem.getTreeView(FileSystem.LOCAL_ROOT);
		localFileSystemTreeView.rootProperty().bind(FileSystem.localFileSystem.rootProperty());
	}
	
	public void setMainApp(App app) {
		this.app = app;
	}
}

