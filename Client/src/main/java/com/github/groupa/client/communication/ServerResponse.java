package com.github.groupa.client.communication;

/***
 * Responses over various protocols should be stored in an instance of this class
 *
 */
public class ServerResponse {
	private final long uniqueId;
	
	public ServerResponse(long uniqueId) { this.uniqueId = uniqueId; }
	
	public long getUniqueId() { return uniqueId; }
	
}
