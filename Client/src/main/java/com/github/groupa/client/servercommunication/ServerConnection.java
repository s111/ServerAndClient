package com.github.groupa.client.servercommunication;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.client.Response;
import retrofit.mime.TypedFile;

import com.github.groupa.client.jsonobjects.ImageInfo;
import com.google.inject.Inject;

public class ServerConnection {
	private static final Logger logger = LoggerFactory
			.getLogger(ServerConnection.class);
	private static final Object staticLock = new Object();
	private RESTService restService;

	@Inject
	public ServerConnection(RESTService restService) {
		this.restService = restService;
	}

	public BufferedImage getImage(long id, String size) {
		BufferedImage image;
		Response response = null;

		try {
			if ("xs".equals(size))
				response = restService.getThumbnailXSmall(id);
			else if ("s".equals(size))
				response = restService.getThumbnailSmall(id);
			else if ("m".equals(size))
				response = restService.getThumbnailMedium(id);
			else if ("l".equals(size))
				response = restService.getThumbnailLarge(id);
			else if ("xl".equals(size))
				response = restService.getThumbnailXLarge(id);
			else if ("raw".equals(size))
				response = restService.getImageRaw(id);
			else if ("compressed".equals(size))
				response = restService.getImageCompressed(id);
			else {
				String err = "Invalid image size: " + size;
				logger.error(err);
				throw new RuntimeException(err);
			}
		} catch (ConnectException e) {
			logger.warn("Could not connect to server to get image(" + id
					+ "): " + e.getMessage());
		} catch (Exception e) {
			logger.warn("Unknown server error when getting image(" + id
					+ ") of size " + size + " : " + e.getMessage());
		}

		if (response != null) {
			try {
				InputStream imageRawStream = response.getBody().in();

				synchronized (staticLock) {
					image = ImageIO.read(imageRawStream);
				}
				imageRawStream.close();

				return image;
			} catch (IOException exception) {
				logger.warn("Failed to load image: " + exception.getMessage());
			}
		}
		return null;
	}

	public boolean rate(long id, int rating) {
		try {
			Response response = restService.rateImage(id, rating);
			if (response.getStatus() == 200)
				return true;
		} catch (ConnectException e) {
			logger.warn("Could not connect to server to rate image(" + id
					+ "): " + e.getMessage());
		} catch (Exception e) {
			logger.warn("Unknown server error when rating image(" + id + "): "
					+ e.getMessage());
		}
		return false;
	}

	public boolean addTag(long id, String tag) {
		try {
			Response response = restService.tagImage(id, tag);
			if (response.getStatus() == 200)
				return true;
		} catch (ConnectException e) {
			logger.warn("Could not connect to server to tag image(" + id
					+ "): " + e.getMessage());
		} catch (Exception e) {
			logger.warn("Unknown server error when tagging image(" + id + "): "
					+ e.getMessage());
		}
		return false;
	}

	public boolean describe(long id, String description) {
		try {
			Response response = restService.describeImage(id, description);
			if (response.getStatus() == 200)
				return true;
		} catch (ConnectException e) {
			logger.warn("Could not connect to server to describe image(" + id
					+ "): " + e.getMessage());
		} catch (Exception e) {
			logger.warn("Unknown server error when describing image(" + id
					+ "): " + e.getMessage());
		}
		return false;
	}

	public boolean crop(long id, Rectangle rectangle) {
		try {
			Response response = restService.cropImage(id, rectangle.x,
					rectangle.y, rectangle.width, rectangle.height);

			if (response != null && response.getStatus() == 200) {
				return true;
			}
		} catch (ConnectException e) {
			logger.warn("Could not connect to server to crop image(" + id
					+ "): " + e.getMessage());
		} catch (Exception e) {
			logger.warn("Unknown server error when cropping image(" + id
					+ "): " + e.getMessage());
		}
		return false;
	}

	public boolean rotate(long id, int angle) {
		try {
			Response response = restService.rotateImage(id, angle);

			if (response != null && response.getStatus() == 200) {
				return true;
			}
		} catch (ConnectException e) {
			logger.warn("Could not connect to server to crop image(" + id
					+ "): " + e.getMessage());
		} catch (Exception e) {
			logger.warn("Unknown server error when cropping image(" + id
					+ "): " + e.getMessage());
		}
		return false;
	}

	public ImageInfo getImageInfo(long id) {
		try {
			return restService.getImageInfo(id);
		} catch (ConnectException e) {
			logger.warn("Could not connect to server to get image info(" + id
					+ "): " + e.getMessage());
		} catch (Exception e) {
			logger.warn("Unknown server error when getting image info(" + id
					+ "): " + e.getMessage());
		}

		return null;
	}

	public ImageInfo uploadImage(File file) {
		try {
			return restService.uploadImage(new TypedFile("image/*", file));
		} catch (ConnectException e) {
			logger.warn("Could not connect to server to upload image("
					+ file.getAbsolutePath() + "): " + e.getMessage());
		} catch (Exception e) {
			logger.warn("Unknown server error when uploading image("
					+ file.getAbsolutePath() + "): " + e.getMessage());
		}
		return null;
	}
}
