package com.github.groupa.client;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

// Feel free to come up with a better name
public class ImageRequester {
	public static Image requestImage(long id) throws IOException {
		CloseableHttpResponse response = sendImageRequest(id);
		Image image = extractImageFromResponse(response);

		return image;
	}

	private static Image extractImageFromResponse(CloseableHttpResponse response)
			throws IOException {
		HttpEntity entity = response.getEntity();

		BufferedInputStream imageStream = new BufferedInputStream(
				entity.getContent());

		Image image = ImageIO.read(imageStream);

		EntityUtils.consume(entity);

		response.close();

		return image;
	}

	private static CloseableHttpResponse sendImageRequest(long id)
			throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet("http://localhost:9000/api/image/" + id);

		CloseableHttpResponse response = httpclient.execute(httpGet);

		return response;
	}
}
