package metadata;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MetadataTest {

	String imagePath = "res/testImage.jpg";

	@Test
	public void setTitle_GivenThisIsAFineTitle_AssertThisIsAFineTitle() {
		ExifWriter exifWriter = new ExifWriter(imagePath);
		exifWriter.setTitle("This is a fine title");
		exifWriter.writeToImage();

		ExifReader exifReader = new ExifReader(imagePath);
		assertEquals("This is a fine title", exifReader.getTitle());
	}

	@Test
	public void setRating_Given5_Assert5() {
		ExifWriter exifWriter = new ExifWriter(imagePath);
		exifWriter.setRating(5);
		exifWriter.writeToImage();

		ExifReader exifReader = new ExifReader(imagePath);
		assertEquals(5, exifReader.getRating());
	}

	@Test
	public void setTags_GivenMagic_Sorcery_Wizardry_AssertMagic_Sorcery_Wizardry() {
		ExifWriter exifWriter = new ExifWriter(imagePath);
		exifWriter.setTags("Magic, Sorcery, Wizardry");
		exifWriter.writeToImage();

		ExifReader exifReader = new ExifReader(imagePath);
		assertEquals("Magic, Sorcery, Wizardry", exifReader.getTags());
	}

	@Test
	public void setDescription_GivenThisImageIsIndescribable_AssertThisImageIsIndescribable() {
		ExifWriter exifWriter = new ExifWriter(imagePath);
		exifWriter.setDescription("This image is indescribable!");
		exifWriter.writeToImage();

		ExifReader exifReader = new ExifReader(imagePath);
		assertEquals("This image is indescribable!", exifReader.getDescription());
	}

}
