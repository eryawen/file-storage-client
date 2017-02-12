package lab.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class FolderMetadata implements Serializable {
	
	private static final long serialVersionUID = 313897513;
	
	private Integer folderId;
	private Integer parentFolderId;
	private String name;
	private String pathLower;
	private String pathDisplay;
	private String serverCreatedAt;
	private Integer ownerId;
}
