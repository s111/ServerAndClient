package thumbnail.generator;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import models.Image;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FilenameUtils;

import queryDB.QueryThumbnail;
import upload.Uploader;
import utils.HibernateUtil;

import com.google.common.collect.ImmutableMap;

public class ThumbnailGenerator {
	private static final Object staticLock = new Object();

	private static final Map<Integer, Dimension> THUMBNAIL_SIZES = ImmutableMap
			.of(0, new Dimension(48, 48), 1, new Dimension(64, 64), 2,
					new Dimension(128, 128), 3, new Dimension(192, 192), 4,
					new Dimension(256, 256));

	private Image image;

	private File file;

	private int size;

	private String filename;

	private Dimension dimension;

	public ThumbnailGenerator(Image image, int size) {
		this.image = image;
		this.file = new File(image.getFilename());
		this.size = size;

		this.filename = generateFilename();
	}

	private Dimension getImageSize() throws IOException {
		BufferedImage bufferedImage = null;

		while (bufferedImage == null) {
			synchronized (staticLock) {
				bufferedImage = ImageIO.read(file);
			}
		}

		return new Dimension(bufferedImage.getWidth(),
				bufferedImage.getHeight());
	}

	public void writeThumbnailToDisk() throws IOException {
		Dimension imageSize = getImageSize();

		if (size < 5) {
			dimension = THUMBNAIL_SIZES.get(size);
		} else {
			dimension = imageSize;
		}

		int width = dimension.width;
		int height = dimension.height;

		if (dimension.width > width) {
			width = imageSize.width;
		}

		if (dimension.height > height) {
			height = imageSize.height;
		}

		synchronized (staticLock) {
			Thumbnails.of(file).size(width, height).toFile(filename);
		}
	}

	public void saveThumbnailToDatabase() {
		QueryThumbnail queryThumbnail = new QueryThumbnail(
				HibernateUtil.getSessionFactory());
		queryThumbnail.addThumbnail(image.getId(), size, filename);
	}

	private String generateFilename() {
		String extension = FilenameUtils.getExtension(file.getAbsolutePath());

		return Uploader.IMAGE_DIRECTORY + "thumb" + image.getId() + "size"
				+ size + "." + extension;
	}
}
