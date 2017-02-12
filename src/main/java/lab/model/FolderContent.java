package lab.model;

import lombok.Data;

import java.util.List;

@Data
public class FolderContent {
	FolderMetadata root;
	List<FolderContent> folders;
	List<FileMetadata> files;
	
	@Override
	public String toString() {
		return root.getPathDisplay();
	}
}
