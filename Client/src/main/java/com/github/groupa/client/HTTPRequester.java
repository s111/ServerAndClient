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

// Feel free to come up with better names
public class HTTPRequester implements Requester {
	private String host;

	public HTTPRequester(String host) {
		this.host = host;
	}

	public Image getImage(long id, String size) throws IOException {
		CloseableHttpResponse response = sendImageRequest("http://" + host
				+ "/api/images/" + id + "/" + size);
		Image image = extractImageFromResponse(response);

		return image;
	}

	private Image extractImageFromResponse(CloseableHttpResponse response)
			throws IOException {
		HttpEntity entity = response.getEntity();

		BufferedInputStream imageStream = new BufferedInputStream(
				entity.getContent());

		Image image = ImageIO.read(imageStream);

		EntityUtils.consume(entity);

		response.close();

		return image;
	}

	private CloseableHttpResponse sendImageRequest(String href)
			throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet(href);
		return httpclient.execute(httpGet);
	}
}
