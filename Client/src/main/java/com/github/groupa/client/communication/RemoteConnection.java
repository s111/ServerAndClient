package com.github.groupa.client.communication;

import java.io.InputStream;
import java.util.Queue;

public abstract class RemoteConnection {
	abstract public ServerResponse commit(long uniqueId, Queue<String> changeLog);
	abstract public ServerResponse getImage(long uniqueId, String param);
	abstract public ServerResponse upload(long uniqueId, InputStream instream);
	abstract public ServerResponse getInfo(long uniqueId);
	abstract public ServerResponse upload(InputStream instream);
}
