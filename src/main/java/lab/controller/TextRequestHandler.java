package lab.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import lab.model.RemotePath;
import lab.request.*;

import java.nio.file.Path;
import java.util.HashMap;

public class TextRequestHandler {
	private HashMap<String, Request> requestMap = new HashMap<>();
	
	public TextRequestHandler(Path local, RemotePath remote, String sessionHeader) {
		requestMap.put("mkdir", new CreateDirectory(remote, sessionHeader));
		requestMap.put("ls", new ListFolderContent(remote, sessionHeader, true));
		requestMap.put("rm", new Delete(remote, sessionHeader));
		requestMap.put("get_met", new GetMetadata(remote, sessionHeader));
		requestMap.put("put", new Upload(local, remote, sessionHeader));
		requestMap.put("get", new Download(local, remote, sessionHeader));
	}
	
	public Object sendRequest(String request) throws UnirestException {
		return requestMap.get(request).doRequest();
	}
}
