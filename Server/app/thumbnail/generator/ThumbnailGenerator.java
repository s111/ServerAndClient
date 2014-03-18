package thumbnail.generator;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import models.ImageModel;
import models.ThumbnailModel;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FilenameUtils;

import upload.Uploader;

import com.google.common.collect.ImmutableMap;

public class ThumbnailGenerator {
	private static final Map<Integer, Dimension> THUMBNAIL_SIZES = ImmutableMap
			.of(0, new Dimension(48, 48), 1, new Dimension(64, 64), 2,
					new Dimension(128, 128), 3, new Dimension(192, 192), 4,
					new Dimension(256, 256));

	private ImageModel imageModel;

	private File image;

	private int size;

	private String filename;

	private Dimension dimension;

	public ThumbnailGenerator(ImageModel imageModel, int size) {
		this.imageModel = imageModel;
		this.image = new File(imageModel.filename);
		this.size = size;

		this.filename = generateFilename();
	}

	private Dimension getImageSize() throws IOException {
		BufferedImage bufferedImage = ImageIO.read(image);

		return new Dimension(bufferedImage.getWidth(),
				bufferedImage.getHeight());
	}

	public void writeThumbnailToDisk() throws IOException {
		if (size < 5) {
			this.dimension = THUMBNAIL_SIZES.get(size);
		} else {
			this.dimension = getImageSize();
		}

		Thumbnails.of(image).size(dimension.width, dimension.height)
				.toFile(filename);
	}

	public ThumbnailModel saveThumbnailToDatabase() {
		ThumbnailModel thumbnailModel = ThumbnailModel.create(imageModel,
				filename, size);

		return thumbnailModel;
	}

	private String generateFilename() {
		String baseName = FilenameUtils.getBaseName(image.getAbsolutePath());
		String extension = FilenameUtils.getExtension(image.getAbsolutePath());

		return Uploader.IMAGE_DIRECTORY + "thumb" + baseName + "size" + size
				+ "." + extension;
	}
}