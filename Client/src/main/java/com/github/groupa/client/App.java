package com.github.groupa.client;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.BasicConfigurator;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	private static void getImages(String host) throws IOException { // TODO: Fix
																	// this mess
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet("http://" + host
				+ "/api/images?limit=9999");

		CloseableHttpResponse response = httpclient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		if (entity.getContentType().getValue().startsWith("application/json")) {
			long id;
			String href;
			Requester requester = new ImageRequester();
			JsonFactory jsonFactory = new JsonFactory();
			JsonParser jp = jsonFactory.createJsonParser(entity.getContent());
			JsonToken tok = jp.nextToken();
			while (tok != null && tok != JsonToken.START_ARRAY) {
				tok = jp.nextToken();
			}
			while (tok != null && tok != JsonToken.END_ARRAY) {
				if (tok == JsonToken.START_OBJECT) {
					id = -1;
					href = null;
					while (tok != null && tok != JsonToken.END_OBJECT) {
						tok = jp.nextToken();
						if (tok == JsonToken.VALUE_NUMBER_INT) {
							id = jp.getValueAsLong();
						} else if (tok == JsonToken.VALUE_STRING) {
							href = jp.getText();
						}
					}
					if (id != -1 && href != null) {
						Library.add(id, requester.requestImage(href));
					}
				}
				tok = jp.nextToken();
			}
		}
	}

	public static void main(String[] args) {
		/*
		 * Quick and simple way to configure slf4j Should probably use a config
		 * as we expand
		 */
		BasicConfigurator.configure();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame mainFrame = new MainFrame("App");
				String host = JOptionPane.showInputDialog("Host address",
						"localhost:9000");

				mainFrame.display();

				try {
					getImages(host);
				} catch (IOException e) {
					logger.warn("Could not import image due to IOException");
				}

				mainFrame.setImageView();
			}
		});
	}
}
