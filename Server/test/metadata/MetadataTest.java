package metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.junit.Test;

public class MetadataTest {

	File image = new File("res/testImage.jpg");

	@Test
	public void setRating_Given5_Assert5() {
		ExifWriter exifWriter = getExifWrite(image);
		exifWriter.setRating(5);

		saveExifWriter(exifWriter);

		ExifReader exifReader = getExifReader(image);

		assertEquals(5, exifReader.getRating());
	}

	@Test
	public void setTags_GivenMagic_Sorcery_Wizardry_AssertMagic_Sorcery_Wizardry()
			throws ImageReadException, IOException {
		ExifWriter exifWriter = getExifWrite(image);
		exifWriter.setTags("Magic, Sorcery, Wizardry");

		saveExifWriter(exifWriter);

		ExifReader exifReader = getExifReader(image);

		assertEquals("Magic, Sorcery, Wizardry", exifReader.getTags());
	}

	@Test
	public void setDescription_GivenThisImageIsIndescribable_AssertThisImageIsIndescribable()
			throws ImageReadException, IOException {
		ExifWriter exifWriter = getExifWrite(image);
		exifWriter.setDescription("This image is indescribable!");

		saveExifWriter(exifWriter);

		ExifReader exifReader = getExifReader(image);

		assertEquals("This image is indescribable!",
				exifReader.getDescription());
	}

	private ExifReader getExifReader(File image) {
		ExifReader exifReader = null;

		try {
			exifReader = new ExifReader(image);
		} catch (ImageReadException exception) {
			fail("Failed to construct ExifReader due to a ImageReadException");
		} catch (IOException exception) {
			fail("Failed to construct ExifReader due to a IOException");
		}

		return exifReader;
	}

	private ExifWriter getExifWrite(File image) {
		ExifWriter exifWriter = null;

		try {
			exifWriter = new ExifWriter(image);
		} catch (ImageReadException e) {
			fail("Failed to construct ExifWriter due to a ImageReadException");
		} catch (ImageWriteException e) {
			fail("Failed to construct ExifWriter due to a ImageWriteException");
		} catch (IOException e) {
			fail("Failed to construct ExifWriter due to a IOException");
		}

		return exifWriter;
	}

	private void saveExifWriter(ExifWriter exifWriter) {
		try {
			exifWriter.save();
		} catch (ImageReadException e) {
			fail("Failed to save due to a ImageReadException");
		} catch (ImageWriteException e) {
			fail("Failed to save due to a ImageWriteException");
		} catch (IOException e) {
			fail("Failed to save due to a IOException");
		}
	}
}