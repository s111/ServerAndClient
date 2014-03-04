package metadata;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.junit.Test;

public class ExifReaderTest {
	@Test
	public void read_metadata_with_rating_5_expect_rating_5()
			throws ImageReadException {
		TiffImageMetadata exifMock = mock(TiffImageMetadata.class);
		ExifReader exifReader = new ExifReader(mock(File.class), exifMock);

		short[] ratingMetadata = { 5 };

		when(exifMock.getFieldValue(TiffConstants.EXIF_TAG_RATING)).thenReturn(
				ratingMetadata);

		exifReader.readMetadata();

		assertEquals(5, exifReader.getRating());
	}

	@Test
	public void read_metadata_with_tags_magic_sorcery_wizardry_expect_tags_magic_sorcery_wizardry()
			throws ImageReadException {
		TiffImageMetadata exifMock = mock(TiffImageMetadata.class);
		ExifReader exifReader = new ExifReader(mock(File.class), exifMock);

		when(exifMock.getFieldValue(TiffConstants.EXIF_TAG_XPKEYWORDS))
				.thenReturn("magic,sorcery,wizardry");

		exifReader.readMetadata();

		assertEquals("magic,sorcery,wizardry", exifReader.getTags());
	}

	@Test
	public void read_metadata_with_description_abc_expect_description_abc()
			throws ImageReadException {
		TiffImageMetadata exifMock = mock(TiffImageMetadata.class);
		ExifReader exifReader = new ExifReader(mock(File.class), exifMock);

		when(exifMock.getFieldValue(TiffConstants.EXIF_TAG_XPCOMMENT))
				.thenReturn("abc");

		exifReader.readMetadata();

		assertEquals("abc", exifReader.getDescription());
	}
}