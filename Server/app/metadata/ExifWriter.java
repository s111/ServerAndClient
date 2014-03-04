package metadata;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.io.Files;

;

public class ExifWriter {

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
	 * image to edit (exif) metadata and writing it back into the file.
	 * 
	 * @param imagePath
	 *            an absolute URL giving the base location of the image
	 * @throws IOException
	 * @throws ImageReadException
	 * @throws ImageWriteException
	 */
	public ExifWriter(File image) throws ImageReadException, IOException,
			ImageWriteException {
		this.image = image;

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
		TiffImageMetadata exif = getExif();

		if (exif != null) {
			outputSet = exif.getOutputSet();
		}

		if (outputSet == null) {
			outputSet = new TiffOutputSet();
		}
	}

	private TiffImageMetadata getExif() throws ImageReadException,
			IOException {
		IImageMetadata metadata = Imaging.getMetadata(image);
		JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
		TiffImageMetadata exif = null;

		if (jpegMetadata != null) {
			exif = jpegMetadata.getExif();
		}
		return exif;
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

			Files.move(tempImage, image);
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
