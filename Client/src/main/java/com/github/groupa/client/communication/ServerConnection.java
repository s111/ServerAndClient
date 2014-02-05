package com.github.groupa.client.communication;
import com.github.groupa.client.image.Image;

import java.util.Queue;

/***
 * Layer between different available protocols
 */
public class ServerConnection {
	public ServerResponse commit(long uniqueId, Queue<String> changeLog) {
		ServerResponse response = null;
		return response;
	}
	
	public ServerResponse getImage(long uniqueId, String param) {
		ServerResponse response = null;
		return response;
	}
	
	public ServerResponse upload(long uniqueId, Image image) { //TODO Use stream
		ServerResponse response = null;
		return response;
	}
	
	public ServerResponse getInfo(long uniqueId) {
		ServerResponse response = null;
		return response;
	}
	
	public ServerResponse upload(Image image) { //TODO Use stream
		ServerResponse response = null;
		return response;
	}
}
