package models;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;

import controllers.ImageUploader;

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
		String filename = ImageUploader.IMAGE_DIRECTORY + "01.png";
		int thumbnailSize = 1;

		ImageModel imageModel = ImageModel.create(filename);

		ThumbnailModel.create(imageModel, ImageUploader.IMAGE_DIRECTORY
				+ "01s.png", thumbnailSize);

		assertEquals(imageModel,
				ThumbnailModel.get(imageModel.id, thumbnailSize).image);
	}

	@Test
	public void add_three_thumbnails_to_image_expect_image_to_contain_three_thumbnails() {
		String filename = ImageUploader.IMAGE_DIRECTORY + "01.png";
		int xs = 0;
		int s = 1;
		int m = 2;

		ImageModel imageModel = ImageModel.create(filename);

		long id = imageModel.id;

		ThumbnailModel.create(imageModel, ImageUploader.IMAGE_DIRECTORY
				+ "01s.png", xs);
		ThumbnailModel.create(imageModel, ImageUploader.IMAGE_DIRECTORY
				+ "02s.png", s);
		ThumbnailModel.create(imageModel, ImageUploader.IMAGE_DIRECTORY
				+ "03s.png", m);

		assertEquals(imageModel, ThumbnailModel.get(id, xs).image);
		assertEquals(imageModel, ThumbnailModel.get(id, s).image);
		assertEquals(imageModel, ThumbnailModel.get(id, m).image);
	}
}
