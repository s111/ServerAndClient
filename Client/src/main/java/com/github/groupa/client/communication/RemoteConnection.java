package com.github.groupa.client.communication;

import java.util.Queue;

import com.github.groupa.client.image.Image;

public abstract class RemoteConnection {
	abstract public ServerResponse commit(long uniqueId, Queue<String> changeLog);
	abstract public ServerResponse getImage(long uniqueId, String param);
	abstract public ServerResponse upload(long uniqueId, Image image);
	abstract public ServerResponse getInfo(long uniqueId);
	abstract public ServerResponse upload(Image image);
}
