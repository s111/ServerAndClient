package metadata;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
import org.apache.commons.imaging.util.IoUtils;

public class ExifWriter {

	private File jpegImageFile;
	private File tempOutputFile;
	private OutputStream outputStream;
	private TiffOutputSet outputSet;
	private TiffOutputDirectory exifDirectory;
	private boolean canThrow;

	private IImageMetadata metadata;
	private static JpegImageMetadata jpegMetadata;

	/**
	 * ExifWriter constructor. Allows you to use its methods on your specified
	 * image to edit (exif) metadata and writing it back into the file.
	 * 
	 * @param imagePath
	 *            an absolute URL giving the base location of the image
	 */
	public ExifWriter(String imagePath) {
		jpegImageFile = new File(imagePath);
		tempOutputFile = new File(imagePath + ".temp");
		outputStream = null;
		canThrow = false;
		try {
			outputSet = null;
			metadata = Imaging.getMetadata(jpegImageFile);
			jpegMetadata = (JpegImageMetadata) metadata;
			if (jpegMetadata != null) {
				TiffImageMetadata exif = jpegMetadata.getExif();
				if (exif != null) {
					outputSet = exif.getOutputSet();
				}
			}
			if (outputSet == null) {
				outputSet = new TiffOutputSet();
			}
			exifDirectory = outputSet.getOrCreateExifDirectory();
		} catch (ImageReadException e) {
			e.printStackTrace();
		} catch (ImageWriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("File not found: " + imagePath);
			return;
		}
	}

	/**
	 * Writes both existing and changed 'EXIF Metadata' to original file.
	 */
	public void writeToImage() {
		try {
			outputStream = new FileOutputStream(tempOutputFile);
			outputStream = new BufferedOutputStream(outputStream);
			new ExifRewriter().updateExifMetadataLossless(jpegImageFile, outputStream, outputSet);
			jpegImageFile.delete();
			tempOutputFile.renameTo(jpegImageFile);
			canThrow = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ImageReadException e) {
			e.printStackTrace();
		} catch (ImageWriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				IoUtils.closeQuietly(canThrow, outputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setTitle(String title) {
		try {
			exifDirectory.removeField(TiffConstants.EXIF_TAG_XPTITLE);
			exifDirectory.add(TiffConstants.EXIF_TAG_XPTITLE, title);
		} catch (ImageWriteException e) {
			e.printStackTrace();
		}
	}

	public void setRating(int rating) {
		try {
			short ratingShort = Short.parseShort(Integer.toString(rating));
			exifDirectory.removeField(TiffConstants.EXIF_TAG_RATING);
			exifDirectory.add(TiffConstants.EXIF_TAG_RATING, ratingShort);
		} catch (ImageWriteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set tags in image metadata
	 * 
	 * @param tags
	 *            Seperate tags by commas (e.g. "tag1, tag2, tag3")
	 */
	public void setTags(String tags) {
		try {
			exifDirectory.removeField(TiffConstants.EXIF_TAG_XPKEYWORDS);
			exifDirectory.add(TiffConstants.EXIF_TAG_XPKEYWORDS, tags);
		} catch (ImageWriteException e) {
			e.printStackTrace();
		}
	}

	public void setDescription(String description) {
		try {
			exifDirectory.removeField(TiffConstants.EXIF_TAG_XPCOMMENT);
			exifDirectory.add(TiffConstants.EXIF_TAG_XPCOMMENT, description);
		} catch (ImageWriteException e) {
			e.printStackTrace();
		}
	}
}
