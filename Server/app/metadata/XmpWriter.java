package metadata;

import java.io.File;

import play.Logger;
import utils.XmpUtil;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.XMPSchemaRegistry;
import com.adobe.xmp.options.PropertyOptions;
import com.adobe.xmp.properties.XMPProperty;

public class XmpWriter {
	private static final String DC_NAMESPACE_URI = "http://purl.org/dc/elements/1.1/";
	private static final String MICROSOFT_NAMESPACE_URI = "http://ns.microsoft.com/photo/1.0/";

	private static final String TITLE = "dc:title";
	private static final String KEYWORDS = "MicrosoftPhoto:LastKeywordXMP";
	private static final String RATING = "MicrosoftPhoto:Rating";

	public static void setDescription(File image, String description) {
		String imageFilePath = image.getAbsolutePath();

		XMPMeta xmpMeta = XmpUtil.extractOrCreateXMPMeta(imageFilePath);

		if (xmpMeta.doesArrayItemExist(DC_NAMESPACE_URI, TITLE, 1)) {
			try {
				xmpMeta.setArrayItem(DC_NAMESPACE_URI, TITLE, 1, description);
			} catch (XMPException e) {
				Logger.warn("Could not set description for image: "
						+ image.getAbsolutePath());

				return;
			}
		} else {
			try {
				xmpMeta.appendArrayItem(DC_NAMESPACE_URI, TITLE,
						new PropertyOptions().setArrayAlternate(true),
						description, null);
			} catch (XMPException e) {
				Logger.warn("Could not set description for image: "
						+ image.getAbsolutePath());

				return;
			}
		}

		XmpUtil.writeXMPMeta(imageFilePath, xmpMeta);
	}

	public static void setRating(File image, int rating) {
		String ratingPercent;

		if (rating == 1) {
			ratingPercent = "1";
		} else if (rating == 2) {
			ratingPercent = "25";
		} else if (rating == 3) {
			ratingPercent = "50";
		} else if (rating == 4) {
			ratingPercent = "75";
		} else if (rating == 5) {
			ratingPercent = "99";
		} else {
			return;
		}

		XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
		try {
			registry.registerNamespace(MICROSOFT_NAMESPACE_URI,
					"MicrosoftPhoto:");
		} catch (XMPException e) {
			Logger.warn("Exception while registering microsoft namespace.\nCould not set rating for image: "
					+ image.getAbsolutePath());

			return;
		}

		String imageFilePath = image.getAbsolutePath();

		XMPMeta xmpMeta = XmpUtil.extractOrCreateXMPMeta(imageFilePath);
		try {
			xmpMeta.setProperty(MICROSOFT_NAMESPACE_URI, RATING, ratingPercent);
		} catch (XMPException e) {
			Logger.warn("Could not set rating for image: "
					+ image.getAbsolutePath());

			return;
		}

		XmpUtil.writeXMPMeta(imageFilePath, xmpMeta);
	}

	public static void addTag(File image, String tag) {
		XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
		try {
			registry.registerNamespace(MICROSOFT_NAMESPACE_URI,
					"MicrosoftPhoto:");
		} catch (XMPException e1) {
			Logger.warn("Exception while registering microsoft namespace.\nCould not add tag to image: "
					+ image.getAbsolutePath());

			return;
		}

		String imageFilePath = image.getAbsolutePath();

		XMPMeta xmpMeta = XmpUtil.extractOrCreateXMPMeta(imageFilePath);

		deleteTag(image, tag);

		try {
			xmpMeta.appendArrayItem(MICROSOFT_NAMESPACE_URI, KEYWORDS,
					new PropertyOptions().setArray(true), tag, null);
		} catch (XMPException e) {
			Logger.warn("Could not add tag to image: "
					+ image.getAbsolutePath());

			return;
		}

		XmpUtil.writeXMPMeta(imageFilePath, xmpMeta);
	}

	public static void deleteTag(File image, String tag) {
		XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
		try {
			registry.registerNamespace(MICROSOFT_NAMESPACE_URI,
					"MicrosoftPhoto:");
		} catch (XMPException e1) {
			Logger.warn("Exception while registering microsoft namespace.\nCould not add tag to image: "
					+ image.getAbsolutePath());

			return;
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

			return;
		}

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

					return;
				}

				if (currentTag.getValue().equals(tag)) {
					xmpMeta.deleteArrayItem(MICROSOFT_NAMESPACE_URI, KEYWORDS,
							i);
				}
			}
		}

		XmpUtil.writeXMPMeta(imageFilePath, xmpMeta);
	}
}
