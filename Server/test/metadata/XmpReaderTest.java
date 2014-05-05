package metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class XmpReaderTest {
	private static File jpg03;
	private static File jpg04;
	private static File jpg05;

	private static File testJpg03;
	private static File testJpg04;
	private static File testJpg05;

	@BeforeClass
	public static void setUpFiles() {
		String filename03 = XmpWriterTest.class.getResource("/03.jpg")
				.getPath();
		String testFilename03 = createTestFilename(filename03);

		String filename04 = XmpWriterTest.class.getResource("/04.jpg")
				.getPath();
		String testFilename04 = createTestFilename(filename04);

		String filename05 = XmpWriterTest.class.getResource("/05.jpg")
				.getPath();
		String testFilename05 = createTestFilename(filename05);

		jpg03 = new File(filename03);
		jpg04 = new File(filename04);
		jpg05 = new File(filename05);

		testJpg03 = new File(testFilename03);
		testJpg04 = new File(testFilename04);
		testJpg05 = new File(testFilename05);
	}

	@Test
	public void get_description() {
		makeCopyOfImageForTesting(jpg03, testJpg03);

		String description = "description asd";

		XmpWriter.setDescription(testJpg03, description);

		assertEquals(description, XmpReader.getDescription(testJpg03));
	}

	@Test
	public void get_rating() {
		makeCopyOfImageForTesting(jpg04, testJpg04);

		int rating = 4;

		XmpWriter.setRating(testJpg04, rating);

		assertEquals(rating, XmpReader.getRating(testJpg04));
	}

	@Test
	public void get_tags() {
		makeCopyOfImageForTesting(jpg05, testJpg05);

		String tag1 = "tag1";
		String tag2 = "tag2";

		XmpWriter.addTag(testJpg05, tag1);
		XmpWriter.addTag(testJpg05, tag2);

		List<String> tags = XmpReader.getTags(testJpg05);

		assertEquals(tag1, tags.get(0));
		assertEquals(tag2, tags.get(1));
	}

	@Test
	public void get_tags_when_there_are_no_tags_present() {
		makeCopyOfImageForTesting(jpg05, testJpg05);

		List<String> tags = XmpReader.getTags(testJpg05);

		assertTrue(tags.isEmpty());
	}

	private static String createTestFilename(String path) {
		String filename = path.substring(path.length() - 6, path.length());

		return path.substring(0, path.length() - 6) + "test" + filename;
	}

	private void makeCopyOfImageForTesting(File image, File imageCopy) {
		try {
			FileUtils.copyFile(image, imageCopy);
		} catch (IOException e) {
			fail("Failed to make a copy of image: " + image.getAbsolutePath());
		}
	}
}
