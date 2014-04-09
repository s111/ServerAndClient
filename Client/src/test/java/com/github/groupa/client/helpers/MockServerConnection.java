package com.github.groupa.client.helpers;

import static org.mockito.Mockito.mock;

import com.github.groupa.client.servercommunication.ServerConnection;

import java.awt.Image;
import java.util.List;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.jsonobjects.ImageInfo;
import com.github.groupa.client.servercommunication.ServerConnection;

public class MockServerConnection {
	public boolean returnValue = true;

	private MockServerConnection() {
	}

	public static ServerConnection get() {
		ServerConnection serverConnection = mock(ServerConnection.class);
		return serverConnection;
	}

}
