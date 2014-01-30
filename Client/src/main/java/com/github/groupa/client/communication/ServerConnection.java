package com.github.groupa.client.communication;
import com.github.groupa.client.image.Image;

import java.util.Queue;

/***
 * Layer between different available protocols
 */
public class ServerConnection {
	public static ServerResponse commit(long uniqueId, Queue<String> changeLog) {
		ServerResponse response = null;
		return response;
	}
	
	public static ServerResponse getInfo(long uniqueId) {
		ServerResponse response = null;
		return response;
	}
	
	public static ServerResponse upload(Image image) {
		ServerResponse response = null;
		return response;
	}
}
