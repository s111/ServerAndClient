package com.github.groupa.client.communication;

/***
 * Responses over various protocols should be stored in an instance of this class
 *
 */
public class ServerResponse {
	public static enum Type {
		SUCCESS,
		UNKNOWN_ERROR
	}
	public Type type = Type.UNKNOWN_ERROR;
	private boolean connectionSucceeded = false;
	
	Object content = null;
	
	public long uniqueId;
	
	public boolean connectionSucceeded() {
		return connectionSucceeded;
	}
	public void setConnectionSucceeded() {
		connectionSucceeded = true;
	}
	
	public Object getContent()
	{
		return content;
	}
}
