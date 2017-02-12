package lab.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;

public class SimpleFileTreeItem extends TreeItem<File> {
	private boolean isFirstTimeChildren = true;
	private boolean isFirstTimeLeaf = true;
	private boolean isLeaf;
	
	public static TreeView<File> getTreeView(String root) {
		TreeView newView = new TreeView<>(new SimpleFileTreeItem(new File(root)));
		newView.getRoot().setExpanded(true);
		return newView;
	}
	
	public SimpleFileTreeItem(File f) {
		super(f);
	}
	
	@Override
	public ObservableList<TreeItem<File>> getChildren() {
		if (isFirstTimeChildren) {
			isFirstTimeChildren = false;
			super.getChildren().setAll(buildChildren(this));
		}
		return super.getChildren();
	}
	
	@Override
	public boolean isLeaf() {
		if (isFirstTimeLeaf) {
			isFirstTimeLeaf = false;
			File f = (File) getValue();
			isLeaf = f.isFile();
		}
		
		return isLeaf;
	}
	
	private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> TreeItem) {
		File f = TreeItem.getValue();
		if (f != null && f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null) {
				ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();
				for (TreeItem<File> childFile : children) {
					childFile.setExpanded(true);
				}
				
				for (File childFile : files) {
					SimpleFileTreeItem item = new SimpleFileTreeItem(childFile);
					item.setExpanded(true);
					children.add(item);
				}
				
				return children;
			}
		}
		
		return FXCollections.emptyObservableList();
	}

}

