package metadata;

import java.io.File;
import java.io.IOException;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;

public class ExifReader {
	private TiffImageMetadata exif;

	private int rating;
	private String tags;
	private String description;

	/**
	 * ExifReader constructor. Allows you to use its methods on your specified
	 * image to get/read (exif) metadata.
	 * 
	 * @param File
	 *            image
	 * @param TiffImageMetadata
	 *            exif, use ((JpegImageMetadata)
	 *            Imaging.getMetadata(image).getExif()
	 * @throws ImageReadException
	 * @throws IOException
	 */
	public ExifReader(File image, TiffImageMetadata exif) {
		this.exif = exif;
	}

	public void readMetadata() throws ImageReadException {
		if (exif != null) {
			readRating();

			tags = exif.getFieldValue(TiffConstants.EXIF_TAG_XPKEYWORDS);
			description = exif.getFieldValue(TiffConstants.EXIF_TAG_XPCOMMENT);
		}
	}

	private void readRating() throws ImageReadException {
		short[] exifRating = exif.getFieldValue(TiffConstants.EXIF_TAG_RATING);

		if (exifRating != null && exifRating.length > 0) {
			rating = exifRating[0];
		}
	}

	public int getRating() {
		return rating;
	}

	public String getTags() {
		return tags;
	}

	public String getDescription() {
		return description;
	}
}
