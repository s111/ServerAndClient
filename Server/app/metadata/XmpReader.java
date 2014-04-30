package metadata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import play.Logger;
import utils.XmpUtil;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.XMPSchemaRegistry;
import com.adobe.xmp.properties.XMPProperty;

public class XmpReader {
	private static final String DC_NAMESPACE_URI = "http://purl.org/dc/elements/1.1/";
	private static final String MICROSOFT_NAMESPACE_URI = "http://ns.microsoft.com/photo/1.0/";

	private static final String TITLE = "dc:title";
	private static final String KEYWORDS = "MicrosoftPhoto:LastKeywordXMP";
	private static final String RATING = "MicrosoftPhoto:Rating";

	public static String getDescription(File image) {
		String imageFilePath = image.getAbsolutePath();

		XMPMeta xmpMeta = XmpUtil.extractOrCreateXMPMeta(imageFilePath);

		String description = null;

		try {
			description = xmpMeta.getArrayItem(DC_NAMESPACE_URI, TITLE, 1)
					.getValue();
		} catch (XMPException e) {
			// Ignore exception
		}

		return description;
	}

	public static int getRating(File image) {
		XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
		try {
			registry.registerNamespace(MICROSOFT_NAMESPACE_URI,
					"MicrosoftPhoto:");
		} catch (XMPException e) {
			Logger.warn("Exception while registering microsoft namespace.\nCould get rating for image: "
					+ image.getAbsolutePath());
		}

		String imageFilePath = image.getAbsolutePath();

		XMPMeta xmpMeta = XmpUtil.extractOrCreateXMPMeta(imageFilePath);

		String rating = null;

		try {
			rating = xmpMeta.getPropertyString(MICROSOFT_NAMESPACE_URI, RATING);
		} catch (XMPException e) {
			// Ignore exception
		}

		if (rating.equals("1")) {
			return 1;
		} else if (rating.equals("25")) {
			return 2;
		} else if (rating.equals("50")) {
			return 3;
		} else if (rating.equals("75")) {
			return 4;
		} else if (rating.equals("99")) {
			return 5;
		}

		return 0;
	}

	public static List<String> getTags(File image) {
		XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
		try {
			registry.registerNamespace(MICROSOFT_NAMESPACE_URI,
					"MicrosoftPhoto:");
		} catch (XMPException e1) {
			Logger.warn("Exception while registering microsoft namespace.\nCould not add tag to image: "
					+ image.getAbsolutePath());

			return null;
		}

		String imageFilePath = image.getAbsolutePath();

		XMPMeta xmpMeta = XmpUtil.extractOrCreateXMPMeta(imageFilePath);

		int numTags;

		try {
			numTags = xmpMeta
					.countArrayItems(MICROSOFT_NAMESPACE_URI, KEYWORDS);
		} catch (XMPException e) {
			Logger.warn("Unable to delete tag from image: "
					+ image.getAbsolutePath()
					+ "\nCould not count array items.");

			return null;
		}

		List<String> list = new ArrayList<>();

		for (int i = 1; i <= numTags; i++) {
			if (xmpMeta
					.doesArrayItemExist(MICROSOFT_NAMESPACE_URI, KEYWORDS, i)) {
				XMPProperty currentTag = null;

				try {
					currentTag = xmpMeta.getArrayItem(MICROSOFT_NAMESPACE_URI,
							KEYWORDS, i);
				} catch (XMPException e) {
					Logger.warn("Unable to delete tag from image: "
							+ image.getAbsolutePath());

					continue;
				}

				if (currentTag != null) {
					list.add(currentTag.getValue());
				}
			}
		}

		return list;
	}
}