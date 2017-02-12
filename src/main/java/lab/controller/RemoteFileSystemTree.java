package lab.controller;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lab.model.FolderContent;
import lab.model.RemotePath;

import java.util.HashMap;

public class RemoteFileSystemTree {
	final Gson gson = new Gson();
	HashMap<String, RemotePath.FileType> filesAndFolders = new HashMap<>();
	private TreeView remoteFileSystem;
	private String sessionHeader;
	
	public RemoteFileSystemTree(String sessionHeader) {
		this.sessionHeader = sessionHeader;
	}
	
	public TreeView getUpdatedRemoteFileSystem(FolderContent rootFolderContent) {
		remoteFileSystem = buildRemoteFileSystem(rootFolderContent);
		remoteFileSystem.setRoot(
			   new TreeItem<>(
					 new RemotePath(rootFolderContent.getRoot().getPathDisplay(), RemotePath.FileType.DIR)));
		remoteFileSystem.getRoot().setExpanded(true);
		addChildren(remoteFileSystem.getRoot(), rootFolderContent);
		return remoteFileSystem;
	}
	
	public TreeView<RemotePath> buildRemoteFileSystem(FolderContent folderContent) {
		remoteFileSystem = new TreeView<>(new TreeItem<>(
			   new RemotePath(folderContent.getRoot().getPathDisplay(), RemotePath.FileType.DIR)));
		addChildren(remoteFileSystem.getRoot(), folderContent);
		remoteFileSystem.getRoot().setExpanded(true);
		return remoteFileSystem;
	}
	
	public void addChildren(TreeItem<RemotePath> resource, FolderContent folderContent) {
		ObservableList<TreeItem<RemotePath>> files = FXCollections.observableArrayList();
		folderContent
			   .getFiles()
			   .forEach(
					 file -> {
						 RemotePath r = new RemotePath(file.getPathDisplay(), RemotePath.FileType.FILE);
						 files.add(new TreeItem<>(r));
						 filesAndFolders.put(r.getPath(), r.getFileType());
					 });
		resource.getChildren().addAll(files);
		
		ObservableList<TreeItem<RemotePath>> folders = FXCollections.observableArrayList();
		folderContent
			   .getFolders()
			   .forEach(
					 folder -> {
						 RemotePath r =
							    new RemotePath(folder.getRoot().getPathDisplay(), RemotePath.FileType.DIR);
						 TreeItem<RemotePath> resourceTreeItem = new TreeItem<RemotePath>(r);
						 resourceTreeItem.setExpanded(true);
						 folders.add(resourceTreeItem);
						 filesAndFolders.put(r.getPath(), r.getFileType());
						 addChildren(resourceTreeItem, folder);
					 });
		resource.getChildren().addAll(folders);
	}
	
	RemotePath.FileType getResourceType(String path) {
		return MoreObjects.firstNonNull(filesAndFolders.get(path), RemotePath.FileType.DIR);
	}
}
