package metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.XmpUtil;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;

public class XmpWriterTest {
	private static final String DC_NAMESPACE_URI = "http://purl.org/dc/elements/1.1/";
	private static final String MICROSOFT_NAMESPACE_URI = "http://ns.microsoft.com/photo/1.0/";

	private static final String TITLE = "dc:title";
	private static final String KEYWORDS = "MicrosoftPhoto:LastKeywordXMP";
	private static final String RATING = "MicrosoftPhoto:Rating";

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
	public void set_description() throws XMPException {
		makeCopyOfImageForTesting(jpg03, testJpg03);

		String description = "IMAGE DESCRIPTION";

		XmpWriter.setDescription(testJpg03, description);

		XMPMeta newXmpMeta = XmpUtil
				.extractXMPMeta(testJpg03.getAbsolutePath());

		assertEquals(description,
				newXmpMeta.getProperty(DC_NAMESPACE_URI, TITLE + "[1]")
						.getValue());
	}

	@Test
	public void set_rating() throws XMPException {
		makeCopyOfImageForTesting(jpg04, testJpg04);

		int rating = 2;

		XmpWriter.setRating(testJpg04, rating);

		XMPMeta newXmpMeta = XmpUtil
				.extractXMPMeta(testJpg04.getAbsolutePath());

		assertEquals("25",
				newXmpMeta.getProperty(MICROSOFT_NAMESPACE_URI, RATING)
						.getValue());
	}

	@Test
	public void add_tags() throws XMPException {
		String tag1 = "tag1";
		String tag2 = "tag2";

		makeCopyOfImageForTesting(jpg05, testJpg05);

		XmpWriter.addTag(testJpg05, tag1);
		XmpWriter.addTag(testJpg05, tag2);

		XMPMeta newXmpMeta = XmpUtil.extractOrCreateXMPMeta(testJpg05
				.getAbsolutePath());

		assertEquals(
				tag1,
				newXmpMeta.getProperty(MICROSOFT_NAMESPACE_URI,
						KEYWORDS + "[1]").getValue());

		assertEquals(
				tag2,
				newXmpMeta.getProperty(MICROSOFT_NAMESPACE_URI,
						KEYWORDS + "[2]").getValue());
	}

	@Test
	public void delete_tag() throws XMPException {
		String tag1 = "tag1";
		String tag2 = "tag2";

		String tagToDelete = tag1;

		makeCopyOfImageForTesting(jpg05, testJpg05);

		XmpWriter.addTag(testJpg05, tag1);
		XmpWriter.addTag(testJpg05, tag2);
		XmpWriter.deleteTag(testJpg05, tagToDelete);

		XMPMeta newXmpMeta = XmpUtil
				.extractXMPMeta(testJpg05.getAbsolutePath());

		assertEquals(
				tag2,
				newXmpMeta.getProperty(MICROSOFT_NAMESPACE_URI,
						KEYWORDS + "[1]").getValue());
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
