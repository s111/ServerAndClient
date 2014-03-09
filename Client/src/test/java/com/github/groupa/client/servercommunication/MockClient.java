package com.github.groupa.client.servercommunication;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class MockClient implements Client {
	@Override
	public Response execute(Request request) throws IOException {
		URL url = new URL(request.getUrl());

		String responseString = "";

		if (url.getPath().equals("/api/images")
				&& url.getQuery().equals("offset=0&limit=2")) {
			responseString = "{\"href\":\"http://localhost:9000/api/images?limit=2\",\"first\":\"http://localhost:9000/api/images?limit=2\",\"next\":\"http://localhost:9000/api/images?offset=2&limit=2\",\"previous\":\"http://localhost:9000/api/images?limit=2\",\"last\":\"http://localhost:9000/api/images?offset=64&limit=2\",\"offset\":0,\"limit\":2,\"images\":[{\"id\":1,\"href\":\"http://localhost:9000/api/images/1\"},{\"id\":2,\"href\":\"http://localhost:9000/api/images/2\"}]}";
		}

		return new Response(request.getUrl(), 200, "nothing",
				Collections.<Header> emptyList(), new TypedByteArray(
						"application/json", responseString.getBytes()));
	}
}
