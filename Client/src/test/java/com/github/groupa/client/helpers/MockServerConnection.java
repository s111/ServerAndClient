package com.github.groupa.client.helpers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Image;
import java.util.List;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.ServerConnection;
import com.github.groupa.client.jsonobjects.ImageInfo;

public class MockServerConnection {
	public boolean returnValue = true;
	
	private MockServerConnection() {
	}

	public static ServerConnection get() {
		ServerConnection serverConnection = mock(ServerConnection.class);
		return serverConnection;
	}
	
}
