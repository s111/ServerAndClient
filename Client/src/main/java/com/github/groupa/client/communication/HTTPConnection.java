package com.github.groupa.client.communication;

import org.apache.http.Header;
import org.apache.http.client.fluent.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

/***
 * Layer between different available protocols
 */
public class HTTPConnection extends RemoteConnection {
	
	private String host;
	private String apidir = "/api/image";
	public HTTPConnection(String host) {
		this.host = host;
	}
	
	public ServerResponse commit(long uniqueId, Queue<String> changeLog) {
		ServerResponse response = null;
		return response;
	}

	public ServerResponse getImage(long uniqueId, String param) {
		ServerResponse response = null;
		String req = host + apidir + "/" + uniqueId;
		if (!"".equals(param)) req += "/" + param;
		try {
			HttpResponse resp;
			org.apache.http.client.fluent.Response resp3 = Request.Get(req).execute();
			resp = resp3.returnResponse();
			Content resp2 = resp3.returnContent();
			HttpEntity ent = resp.getEntity();
			InputStream stream = ent.getContent();
			Header header = ent.getContentType();
			response = new ImageResponse(uniqueId);
		} catch (IOException e) {
			response = new ErrorResponse(uniqueId, e);
		}
		return response;
	}

	public ServerResponse upload(long uniqueId, InputStream instream) { // TODO Use stream
		ServerResponse response = null;
		return response;
	}

	public ServerResponse getInfo(long uniqueId) {
		ServerResponse response = null;
		return response;
	}

	public ServerResponse upload(InputStream instream) { // TODO Use stream
		ServerResponse response = null;
		String req = host + apidir + "/image";
		try {
			Response resp = Request.Post(req).bodyStream(instream).execute();
			HttpResponse httpresp = resp.returnResponse();
			
			
		} catch (IOException e) {
			response = new ErrorResponse(e);
		}
		return response;
	}


}
