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
	private TiffImageMetadata exif;
	private int rating;
	private String tags;
	private String description;

	/**
	 * ExifReader constructor. Allows you to use its methods on your specified
	 * image to get/read (exif) metadata.
	 * 
	 * @param imagePath
	 *            an absolute URL giving the base location of the image
	 * @throws ImageReadException
	 * @throws IOException
	 */
	public ExifReader(File jpegImageFile) throws ImageReadException,
			IOException {
		IImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
		JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
		
		exif = jpegMetadata.getExif();

		extractMetadata();
	}

	private void extractMetadata() throws ImageReadException {
		rating = exif.getFieldValue(TiffConstants.EXIF_TAG_RATING)[0];
		tags = exif.getFieldValue(TiffConstants.EXIF_TAG_XPKEYWORDS);
		description = exif.getFieldValue(TiffConstants.EXIF_TAG_XPCOMMENT);
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
