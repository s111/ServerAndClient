package metadata;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import models.Tag;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;

public class ExifReader {
	private TiffImageMetadata exif;

	private int rating;
	private String description;

	private Set<Tag> tags = new HashSet<>();

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
			extractTags();

			description = exif.getFieldValue(TiffConstants.EXIF_TAG_XPCOMMENT);
		}
	}

	private void readRating() throws ImageReadException {
		short[] exifRating = exif.getFieldValue(TiffConstants.EXIF_TAG_RATING);

		if (exifRating != null && exifRating.length > 0) {
			rating = exifRating[0];
		}
	}

	private void extractTags() throws ImageReadException {
		String tagString = exif
				.getFieldValue(TiffConstants.EXIF_TAG_XPKEYWORDS);

		if (tagString == null) {
			return;
		}

		String[] tagList = tagString.split(",");

		for (String tagName : tagList) {
			Tag tag = new Tag();
			tag.setName(tagName);

			tags.add(tag);
		}
	}

	public int getRating() {
		return rating;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public String getDescription() {
		return description;
	}
}
