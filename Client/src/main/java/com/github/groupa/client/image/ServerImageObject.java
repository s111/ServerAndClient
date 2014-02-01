package com.github.groupa.client.image;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.github.groupa.client.communication.ServerResponse;

/***
 * Should store everything about an image as received from server
 */
public class ServerImageObject {
	public LinkedList<String> changeLog = new LinkedList<>(); // In case image have been modified serverside
	public Map<String, Image> images = new HashMap<>();
	
	public ServerImageObject(ServerResponse response) {
		
	}
	
	/***
	 * @param response
	 * @return true if response indicates that there have been made changes
	 */
	public boolean refresh(ServerResponse response) {
		return false;
	}

}
