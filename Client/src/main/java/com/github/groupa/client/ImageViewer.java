package com.github.groupa.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.github.groupa.client.components.ImageDescriptionButton;
import com.github.groupa.client.components.ImageRater;

public class ImageViewer implements KeyListener {
	private JFrame frame;
	
	private Library library = new Library();

	private Container contentPane;

	private String title;

	private JLabel imageLabel = new JLabel("image");

	public ImageViewer(String title) {
		this.title = title;
		
		setUpImageViewer();
		
		setImage(library.getNextImage());
	}

	public void display() {
		frame.setVisible(true);
	}

	private void setUpImageViewer() {
		frame = new JFrame(title);
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.addKeyListener(this);
		contentPane.addNotify();
		contentPane.requestFocus();
		
		addPanelsToContentPane(contentPane);

		frame.pack();
	}

	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		JLabel label = new JLabel("Top Toolbar");

		topPanel.add(label);

		return topPanel;
	}

	private JPanel createPicturePanel() {
		JPanel picturePanel = new JPanel();
		
		picturePanel.add(imageLabel);
		
		return picturePanel;
	}

	private JPanel createLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.add(new ImageDescriptionButton().getButton());

		return leftPanel;
	}

	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		
		bottomPanel.add(new ImageRater().getPanel());

		return bottomPanel;
	}

	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel();
		JLabel label = new JLabel("Right Toolbar");

		rightPanel.add(label);

		return rightPanel;
	}

	private void addPanelsToContentPane(Container contentPane) {
		JPanel topPanel = createTopPanel();
		JPanel picturePanel = createPicturePanel();
		JPanel leftPanel = createLeftPanel();
		JPanel bottomPanel = createBottomPanel();
		JPanel rightPanel = createRightPanel();

		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(picturePanel, BorderLayout.CENTER);
		contentPane.add(leftPanel, BorderLayout.WEST);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		contentPane.add(rightPanel, BorderLayout.EAST);
	}
	
	private void setImage(ImageObject img) {
		if (img == null) {
			return;
		}
		
		imageLabel.setIcon(new ImageIcon(img.getImage()));
	}
	
	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		
	}
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			setImage(library.getNextImage());
			System.out.println("hei");
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			setImage(library.getPrevImage());
			System.out.println("yolo");
		}
	}

	public static void main(String[] args) throws IOException {
		try {
			getImages("localhost:9000");
		} catch (IOException e) {
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ImageViewer imageViewer = new ImageViewer("Title");
				imageViewer.display();
			}
		});
	}
	
	private static void getImages(String host) throws IOException { //TODO: Fix this mess
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet("http://" + host
				+ "/api/images?limit=9999");

		CloseableHttpResponse response = httpclient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		if (entity.getContentType().getValue().startsWith("application/json")) {
			long id;
			String href;
			Requester requester = new HTTPRequester(host);
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
						Library.add(new ImageObject(id, requester));
					}
				}
				tok = jp.nextToken();
			}
		}
	}
}
