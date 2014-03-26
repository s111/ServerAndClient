package metadata;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import models.Tag;

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
import upload.Uploader;

public class ExifWriter {
	private TiffImageMetadata exif;

	private File image;
	private File tempImage;

	private OutputStream outputStream;

	private TiffOutputSet outputSet;
	private TiffOutputDirectory exifDirectory;

	private int rating = -1;
	private Set<Tag> tags = new HashSet<>();
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
		String extension = FilenameUtils.getExtension(filename);

		tempImage = File.createTempFile("tmp", "." + extension, new File(
				Uploader.IMAGE_DIRECTORY));

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

			cleanUp();
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

	public void cleanUp() {
		boolean deleted = FileUtils.deleteQuietly(tempImage);

		if (!deleted) {
			Logger.warn("Failed to delete tempImage file: "
					+ tempImage.getAbsolutePath());
		}
	}

	private void setMetadata() throws ImageWriteException {
		if (rating != -1) {
			setRatingMetadata();
		}

		if (tags != null) {
			ExifReader exifReader = new ExifReader(image, exif);
			tags.addAll(exifReader.getTags());

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
		String tagString = tagsToCommaDelimitedString();

		exifDirectory.removeField(TiffConstants.EXIF_TAG_XPKEYWORDS);
		exifDirectory.add(TiffConstants.EXIF_TAG_XPKEYWORDS, tagString);
	}

	private String tagsToCommaDelimitedString() {
		String tagString = "";

		if (tags.size() == 0)
			return tagString;

		for (Tag tag : tags) {
			tagString += tag.getName() + ",";
		}

		tagString = tagString.substring(0, tagString.length() - 1);

		return tagString;
	}

	private void setRatingMetadata() throws ImageWriteException {
		short ratingShort = Short.parseShort(Integer.toString(rating));

		exifDirectory.removeField(TiffConstants.EXIF_TAG_RATING);
		exifDirectory.add(TiffConstants.EXIF_TAG_RATING, ratingShort);
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
