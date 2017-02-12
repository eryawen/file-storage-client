package lab.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeView;
import lab.controller.RemoteFileSystemTree;
import lombok.Data;

import java.io.File;

@Data
public class FileSystem {
	
	public static final String LOCAL_ROOT = "D:\\";
	public static StringProperty currentLocalFileProperty = new SimpleStringProperty(LOCAL_ROOT);
	public static StringProperty currentRemoteFileProperty =
		   new SimpleStringProperty("");
	public static TreeView<File> localFileSystem;
	public static TreeView<RemotePath> remoteFileSystem;
	public String remoteRoot;
	public static RemoteFileSystemTree remoteFileSystemTree;
	
	public FileSystem(String remoteRoot) {
		this.remoteRoot = remoteRoot;
	}
}

