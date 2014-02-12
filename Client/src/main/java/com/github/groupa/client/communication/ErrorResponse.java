package com.github.groupa.client.communication;

public class ErrorResponse extends ServerResponse {
	public String error;

	public ErrorResponse(long uniqueId, Exception e) {
		super(uniqueId);
		this.error = e.getMessage();
	}

	public ErrorResponse(Exception e) {
		super(-1l);
		this.error = e.getMessage();
	}
	
	public ErrorResponse(long uniqueId, String s) {
		super(uniqueId);
		this.error = s;
	}
}
