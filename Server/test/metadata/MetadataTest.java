package metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.imaging.ImageReadException;
import org.junit.Test;

public class MetadataTest {

	String imagePath = "res/testImage.jpg";

	@Test
	public void setRating_Given5_Assert5() {
		ExifWriter exifWriter = new ExifWriter(imagePath);
		exifWriter.setRating(5);
		exifWriter.writeToImage();

		ExifReader exifReader = getExifReader(new File(imagePath));

		assertEquals(5, exifReader.getRating());
	}

	@Test
	public void setTags_GivenMagic_Sorcery_Wizardry_AssertMagic_Sorcery_Wizardry()
			throws ImageReadException, IOException {
		ExifWriter exifWriter = new ExifWriter(imagePath);
		exifWriter.setTags("Magic, Sorcery, Wizardry");
		exifWriter.writeToImage();

		ExifReader exifReader = getExifReader(new File(imagePath));

		assertEquals("Magic, Sorcery, Wizardry", exifReader.getTags());
	}

	@Test
	public void setDescription_GivenThisImageIsIndescribable_AssertThisImageIsIndescribable()
			throws ImageReadException, IOException {
		ExifWriter exifWriter = new ExifWriter(imagePath);
		exifWriter.setDescription("This image is indescribable!");
		exifWriter.writeToImage();

		ExifReader exifReader = getExifReader(new File(imagePath));

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
}