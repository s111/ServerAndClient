package metadata;

import java.io.File;
import java.sql.Timestamp;
import java.util.Set;

import models.Image;
import models.Tag;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import play.Logger;
import utils.XmpUtil;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.options.PropertyOptions;
import com.adobe.xmp.properties.XMPProperty;

public class XmpWriter {
	private static final String ADOBE_NAMESPACE_URI = "http://ns.adobe.com/xap/1.0/";
	private static final String DC_NAMESPACE_URI = "http://purl.org/dc/elements/1.1/";
	private static final String MICROSOFT_NAMESPACE_URI = "http://ns.microsoft.com/photo/1.0/";

	private static final String CREATE_DATE = "xmp:CreateDate";
	private static final String TITLE = "dc:title";
	private static final String DESCRIPTION = "dc:description";
	private static final String SUBJECT = "dc:subject";
	private static final String KEYWORDS = "MicrosoftPhoto:LastKeywordXMP";
	private static final String RATING = "MicrosoftPhoto:Rating";

	public static void setDescription(File image, String description) {
		String imageFilePath = image.getAbsolutePath();

		XMPMeta xmpMeta = XmpUtil.extractOrCreateXMPMeta(imageFilePath);

		try {
			xmpMeta.setLocalizedText(DC_NAMESPACE_URI, TITLE, "default",
					"default", description);
			xmpMeta.setLocalizedText(DC_NAMESPACE_URI, DESCRIPTION, "default",
					"default", description);
		} catch (XMPException e) {
			Logger.warn("Could not set image metadata title: "
					+ image.getAbsolutePath());
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

	private static void _addTag(File image, String tag) {
		String imageFilePath = image.getAbsolutePath();

		XMPMeta xmpMeta = XmpUtil.extractOrCreateXMPMeta(imageFilePath);

		try {
			xmpMeta.appendArrayItem(MICROSOFT_NAMESPACE_URI, KEYWORDS,
					new PropertyOptions().setArray(true), tag, null);
			xmpMeta.appendArrayItem(DC_NAMESPACE_URI, SUBJECT,
					new PropertyOptions().setArray(true), tag, null);
		} catch (XMPException e) {
			Logger.warn("Could not add tag to image: "
					+ image.getAbsolutePath());

			return;
		}

		XmpUtil.writeXMPMeta(imageFilePath, xmpMeta);
	}

	public static void addTag(File image, String tag) {
		deleteTag(image, tag);
		_addTag(image, tag);
	}

	public static void deleteTag(File image, String tag) {
		String imageFilePath = image.getAbsolutePath();

		XMPMeta xmpMeta = XmpUtil.extractOrCreateXMPMeta(imageFilePath);

		deleteKeywordTag(image, tag, xmpMeta);
		deleteSubjectTag(image, tag, xmpMeta);

		XmpUtil.writeXMPMeta(imageFilePath, xmpMeta);
	}

	private static void deleteKeywordTag(File image, String tag, XMPMeta xmpMeta) {
		int numTags;

		try {
			numTags = xmpMeta
					.countArrayItems(MICROSOFT_NAMESPACE_URI, KEYWORDS);
		} catch (XMPException e) {
			// There are no tags; return.

			return;
		}

		for (int i = 1; i <= numTags; i++) {
			if (xmpMeta
					.doesArrayItemExist(MICROSOFT_NAMESPACE_URI, KEYWORDS, i)) {
				XMPProperty currentKeywordTag = null;

				try {
					currentKeywordTag = xmpMeta.getArrayItem(
							MICROSOFT_NAMESPACE_URI, KEYWORDS, i);
				} catch (XMPException e) {
					Logger.warn("Unable to delete tag from image: "
							+ image.getAbsolutePath());

					return;
				}

				if (currentKeywordTag.getValue().equals(tag)) {
					xmpMeta.deleteArrayItem(MICROSOFT_NAMESPACE_URI, KEYWORDS,
							i);
				}
			}
		}
	}

	private static void deleteSubjectTag(File image, String tag, XMPMeta xmpMeta) {
		int numTags;

		try {
			numTags = xmpMeta.countArrayItems(DC_NAMESPACE_URI, SUBJECT);
		} catch (XMPException e) {
			// There are no tags; return.

			return;
		}

		for (int i = 1; i <= numTags; i++) {
			if (xmpMeta.doesArrayItemExist(DC_NAMESPACE_URI, SUBJECT, i)) {
				XMPProperty currentKeywordTag = null;

				try {
					currentKeywordTag = xmpMeta.getArrayItem(DC_NAMESPACE_URI,
							SUBJECT, i);
				} catch (XMPException e) {
					Logger.warn("Unable to delete tag from image: "
							+ image.getAbsolutePath());

					return;
				}

				if (currentKeywordTag.getValue().equals(tag)) {
					xmpMeta.deleteArrayItem(DC_NAMESPACE_URI, SUBJECT, i);
				}
			}
		}
	}

	public static void setCreationDate(File image, long time) {
		String imageFilePath = image.getAbsolutePath();

		XMPMeta xmpMeta = XmpUtil.extractOrCreateXMPMeta(imageFilePath);
		try {
			String date = new DateTime(time).toDateTimeISO().toString(
					ISODateTimeFormat.dateTime());

			if (date.contains("+")) {
				date = date.substring(0, date.indexOf('+')) + 'Z';
			}

			xmpMeta.setProperty(ADOBE_NAMESPACE_URI, CREATE_DATE, date);
		} catch (XMPException e) {
			Logger.warn("Could not set creation date for image: "
					+ image.getAbsolutePath());

			return;
		}

		XmpUtil.writeXMPMeta(imageFilePath, xmpMeta);
	}

	public static void writeMetadataFromDatabaseToFile(Image image) {
		File file = new File(image.getFilename());

		String description = image.getDescription();

		if (description != null) {
			XmpWriter.setDescription(file, description);
		}

		Integer rating = image.getRating();

		if (rating != null) {
			XmpWriter.setRating(file, rating);
		}

		Set<Tag> tags = image.getTags();

		for (Tag tag : tags) {
			XmpWriter.addTag(file, tag.getName());
		}

		Timestamp dateTaken = image.getDateTaken();

		if (dateTaken != null) {
			XmpWriter.setCreationDate(file, dateTaken.getTime());
		}
	}
}
