package com.github.groupa.client.servercommunication;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.groupa.client.jsonobjects.ImageList;

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

	public ImageList getImageList(int limit) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet("http://" + host + "/api/images?limit="
				+ limit);

		CloseableHttpResponse response = httpclient.execute(httpGet);
		HttpEntity entity = response.getEntity();

		if (entity.getContentType().getValue().startsWith("application/json")) {

			ObjectMapper mapper = new ObjectMapper();

			return mapper.readValue(entity.getContent(), ImageList.class);
		}
		return null;
	}

	@Override
	public boolean rateImage(long id, int rating) throws IOException {
		return post("http://" + host + "/api/images/" + id + "/rate", rating);
	}

	@Override
	public boolean describeImage(long id, String description)
			throws IOException {
		return post("http://" + host + "/api/images/" + id + "/description",
				description);
	}

	private boolean post(String href, Object value) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(href);
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		nvps.add(new BasicNameValuePair("value", "" + value));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response = httpclient.execute(httpPost);

		boolean result = false;
		try {
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				result = true;
			EntityUtils.consume(response.getEntity());
		} finally {
			response.close();
		}
		return result;
	}
}
