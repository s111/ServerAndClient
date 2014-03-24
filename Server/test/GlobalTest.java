import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;

import java.io.File;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Helpers;
import queryDB.QueryImage;
import upload.Uploader;
import utils.HibernateUtil;

public class GlobalTest {
	private SessionFactory sessionFactory;

	@Before
	public void setUp() {
		sessionFactory = HibernateUtil.getNewSessionFactory();

		Helpers.start(fakeApplication());
	}

	@After
	public void tearDown() {
		sessionFactory.close();

		Helpers.stop(fakeApplication());
	}

	@Test
	public void check_initial_database_size_expect_to_match_files_on_disk() {
		File directory = new File(Uploader.IMAGE_DIRECTORY);

		File[] listOfFiles = directory.listFiles();

		if (listOfFiles == null)
			return;

		int size = 0;

		for (File image : listOfFiles) {
			String filename = image.getName();

			if (filename.matches("^(.+).(png|jpg)$")) {
				if (filename.contains("thumb") || filename.contains("tmp")) {
					continue;
				}

				size++;
			}
		}

		QueryImage queryImage = new QueryImage(sessionFactory);

		assertEquals("images in database does not match images on disk", size,
				queryImage.getImages().size());
	}
}
