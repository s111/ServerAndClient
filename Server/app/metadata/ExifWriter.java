package metadata;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import play.Logger;

public class ExifWriter {
	private TiffImageMetadata exif;

	private File image;
	private File tempImage;

	private OutputStream outputStream;

	private TiffOutputSet outputSet;
	private TiffOutputDirectory exifDirectory;

	private int rating = -1;
	private String tags;
	private String description;

	/**
	 * ExifWriter constructor. Allows you to use its methods on your specified
	 * image to edit (exif) metadata and write it back into the file.
	 * 
	 * @param File
	 *            image
	 * @param TiffImageMetadata
	 *            exif, use ((JpegImageMetadata)
	 *            Imaging.getMetadata(image).getExif()
	 * @throws IOException
	 * @throws ImageReadException
	 * @throws ImageWriteException
	 */
	public ExifWriter(File image, TiffImageMetadata exif)
			throws ImageReadException, IOException, ImageWriteException {
		this.image = image;
		this.exif = exif;

		String filename = image.getName();
		String basename = FilenameUtils.getBaseName(filename);
		String extension = FilenameUtils.getExtension(filename);

		tempImage = File.createTempFile(basename, "." + extension, new File(
				System.getProperty("java.io.tmpdir")));

		setUpOutputSet();

		exifDirectory = outputSet.getOrCreateExifDirectory();
	}

	private void setUpOutputSet() throws ImageReadException, IOException,
			ImageWriteException {
		if (exif != null) {
			outputSet = exif.getOutputSet();
		}

		if (outputSet == null) {
			outputSet = new TiffOutputSet();
		}
	}

	/**
	 * Writes both existing and changed 'EXIF Metadata' to original file.
	 * 
	 * @throws IOException
	 * @throws ImageWriteException
	 * @throws ImageReadException
	 */
	public void save() throws ImageReadException, ImageWriteException,
			IOException {
		setMetadata();

		try {
			outputStream = new FileOutputStream(tempImage);
			outputStream = new BufferedOutputStream(outputStream);

			new ExifRewriter().updateExifMetadataLossless(image, outputStream,
					outputSet);

			outputStream.close();

			FileUtils.copyFile(tempImage, image);

			boolean deleted = FileUtils.deleteQuietly(tempImage);

			if (!deleted) {
				Logger.warn("Failed to delete tempImage file: "
						+ tempImage.getAbsolutePath());
			}
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

	private void setMetadata() throws ImageWriteException {
		if (rating != -1) {
			setRatingMetadata();
		}

		if (tags != null) {
			setTagsMetadata();
		}

		if (description != null) {
			setDescriptionMetadata();
		}
	}

	private void setDescriptionMetadata() throws ImageWriteException {
		exifDirectory.removeField(TiffConstants.EXIF_TAG_XPCOMMENT);
		exifDirectory.add(TiffConstants.EXIF_TAG_XPCOMMENT, description);
	}

	private void setTagsMetadata() throws ImageWriteException {
		exifDirectory.removeField(TiffConstants.EXIF_TAG_XPKEYWORDS);
		exifDirectory.add(TiffConstants.EXIF_TAG_XPKEYWORDS, tags);
	}

	private void setRatingMetadata() throws ImageWriteException {
		short ratingShort = Short.parseShort(Integer.toString(rating));

		exifDirectory.removeField(TiffConstants.EXIF_TAG_RATING);
		exifDirectory.add(TiffConstants.EXIF_TAG_RATING, ratingShort);
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * Set tags in image metadata
	 * 
	 * @param tags
	 *            Seperate tags by commas (e.g. "tag1, tag2, tag3")
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
