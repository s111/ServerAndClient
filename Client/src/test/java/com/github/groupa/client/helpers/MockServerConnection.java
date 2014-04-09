package com.github.groupa.client.helpers;

import static org.mockito.Mockito.mock;

import com.github.groupa.client.ServerConnection;

public class MockServerConnection {
	public boolean returnValue = true;

	private MockServerConnection() {
	}

	public static ServerConnection get() {
		ServerConnection serverConnection = mock(ServerConnection.class);
		return serverConnection;
	}

}
