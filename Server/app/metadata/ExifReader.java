package metadata;

import java.io.File;
import java.io.IOException;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;

public class ExifReader {

	private File jpegImageFile;
	private IImageMetadata metadata;
	private static JpegImageMetadata jpegMetadata;
	private static TiffImageMetadata exif;

	/**
	 * ExifReader constructor. Allows you to use its methods on your specified
	 * image to get/read (exif) metadata.
	 * 
	 * @param imagePath
	 *            an absolute URL giving the base location of the image
	 */
	public ExifReader(String imagePath) {
		jpegImageFile = new File(imagePath);
		try {
			metadata = Imaging.getMetadata(jpegImageFile);
			jpegMetadata = (JpegImageMetadata) metadata;
			if (jpegMetadata != null) {
				exif = jpegMetadata.getExif();
			}
		} catch (ImageReadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("File not found: " + imagePath);
			return;
		}
	}

	public String getTitle() {
		String title = null;
		try {
			title = exif.getFieldValue(TiffConstants.EXIF_TAG_XPTITLE);
		} catch (ImageReadException e) {
			e.printStackTrace();
		}

		return title;
	}

	public int getRating() {
		short[] rating = null;
		try {
			rating = exif.getFieldValue(TiffConstants.EXIF_TAG_RATING);
		} catch (ImageReadException e) {
			e.printStackTrace();
		}
		if (rating == null) {
			return -1;
		}
		return rating[0];
	}

	public String getTags() {
		String tags = null;
		try {
			tags = exif.getFieldValue(TiffConstants.EXIF_TAG_XPKEYWORDS);
		} catch (ImageReadException e) {
			e.printStackTrace();
		}
		return tags;
	}

	public String getDescription() {
		String description = null;
		try {
			description = exif.getFieldValue(TiffConstants.EXIF_TAG_XPCOMMENT);
		} catch (ImageReadException e) {
			e.printStackTrace();
		}
		return description;
	}
}
