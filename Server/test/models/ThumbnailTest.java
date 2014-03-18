package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import upload.Uploader;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.google.common.base.Optional;

public class ThumbnailTest {
	@BeforeClass
	public static void startApp() {
		start(fakeApplication());
	}

	@AfterClass
	public static void stopApp() {
		stop(fakeApplication());
	}

	@Before
	public void resetDB() {
		String serverName = "default";

		EbeanServer server = Ebean.getServer(serverName);
		ServerConfig config = new ServerConfig();

		DdlGenerator ddl = new DdlGenerator();
		ddl.setup((SpiEbeanServer) server, new H2Platform(), config);
		ddl.runScript(false, ddl.generateDropDdl());
		ddl.runScript(false, ddl.generateCreateDdl());
	}

	@Test
	public void add_thumbnail_to_image_expect_image_to_contain_one_thumbnail() {
		String filename = Uploader.IMAGE_DIRECTORY + "01.png";

		ImageModel imageModel = ImageModel.create(filename);

		ThumbnailModel.create(imageModel, Uploader.IMAGE_DIRECTORY + "01s.png",
				ThumbnailModel.SMALL);

		Optional<ThumbnailModel> thumbnailSmall = ThumbnailModel.get(
				imageModel.id, ThumbnailModel.SMALL);

		assertTrue(thumbnailSmall.isPresent());
		assertEquals(imageModel, thumbnailSmall.get().image);
	}

	@Test
	public void add_three_thumbnails_to_image_expect_image_to_contain_three_thumbnails() {
		String filename = Uploader.IMAGE_DIRECTORY + "01.png";

		ImageModel imageModel = ImageModel.create(filename);

		long id = imageModel.id;

		ThumbnailModel.create(imageModel, Uploader.IMAGE_DIRECTORY + "01s.png",
				ThumbnailModel.X_SMALL);
		ThumbnailModel.create(imageModel, Uploader.IMAGE_DIRECTORY + "02s.png",
				ThumbnailModel.SMALL);
		ThumbnailModel.create(imageModel, Uploader.IMAGE_DIRECTORY + "03s.png",
				ThumbnailModel.MEDIUM);

		Optional<ThumbnailModel> thumbnailXSmall = ThumbnailModel.get(id,
				ThumbnailModel.X_SMALL);
		Optional<ThumbnailModel> thumbnailSmall = ThumbnailModel.get(id,
				ThumbnailModel.SMALL);
		Optional<ThumbnailModel> thumbnailMedium = ThumbnailModel.get(id,
				ThumbnailModel.MEDIUM);

		assertTrue(thumbnailXSmall.isPresent());
		assertTrue(thumbnailSmall.isPresent());
		assertTrue(thumbnailMedium.isPresent());

		assertEquals(imageModel, thumbnailXSmall.get().image);
		assertEquals(imageModel, thumbnailSmall.get().image);
		assertEquals(imageModel, thumbnailMedium.get().image);
	}
}
