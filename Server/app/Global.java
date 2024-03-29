import java.io.File;
import java.sql.Timestamp;

import metadata.XmpReader;
import models.Image;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import queryDB.QueryImage;
import upload.Uploader;
import utils.HibernateUtil;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.XMPSchemaRegistry;

public class Global extends GlobalSettings {
	private static final String MICROSOFT_NAMESPACE_URI = "http://ns.microsoft.com/photo/1.0/";

	@Override
	public void onStart(Application application) {
		XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
		try {
			registry.registerNamespace(MICROSOFT_NAMESPACE_URI,
					"MicrosoftPhoto:");
		} catch (XMPException e1) {
			Logger.warn("Exception while registering microsoft namespace.");
		}

		File directory = new File(Uploader.IMAGE_DIRECTORY);

		// Set the IMAGE_DIRECTORY to the full path of the directory so that
		// there is no doubt which directory to use
		Uploader.IMAGE_DIRECTORY = directory.getAbsolutePath() + File.separator;

		// Update the directory. This is because when we do path resolution we
		// might be on another level due to the app path being different from
		// where the binaries are
		directory = new File(Uploader.IMAGE_DIRECTORY);

		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				Logger.error("Could not create upload directory");
				return;
			}
		}

		File[] listOfFiles = directory.listFiles();

		for (File file : listOfFiles) {
			String filename = file.getName();

			String filenameInDatabase = Uploader.IMAGE_DIRECTORY + filename;

			if (filename.matches("^(.+).(png|jpg)$")) {
				if (filename.contains("thumb") || filename.contains("tmp")) {
					continue;
				}

				QueryImage queryImage = new QueryImage(
						HibernateUtil.getSessionFactory());

				if (queryImage.getImage(filenameInDatabase) == null) {
					Image image = new Image();
					image.setFilename(filenameInDatabase);
					image.setDateTaken(new Timestamp(XmpReader
							.getCreationDate(new File(filenameInDatabase))));

					queryImage.addImage(image);
				}
			}
		}
	}
}