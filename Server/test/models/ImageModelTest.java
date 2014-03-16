package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

public class ImageModelTest {
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
	public void initial_database_size_expect_0() {
		assertEquals(0, ImageModel.find.all().size());
	}

	@Test
	public void insert_image_expect_to_retrieve_image() {
		String filename = ImageUploader.IMAGE_DIRECTORY + "01.png";

		new ImageModel(filename).save();

		ImageModel imageModel = ImageModel.find.where()
				.eq("filename", filename).findUnique();

		assertNotNull(imageModel);
	}

	@Test
	public void insert_image_expect_database_size_1() {
		String filename = ImageUploader.IMAGE_DIRECTORY + "01.png";

		new ImageModel(filename).save();

		int databaseSize = ImageModel.find.all().size();

		assertEquals(1, databaseSize);
	}

	@Test
	public void create_image_expect_to_retrieve_image() {
		String filename = ImageUploader.IMAGE_DIRECTORY + "01.png";

		ImageModel.create(filename);

		ImageModel imageModel = ImageModel.find.where()
				.eq("filename", filename).findUnique();

		assertNotNull(imageModel);
	}

	@Test
	public void create_image_expect_to_get_image() {
		String filename = ImageUploader.IMAGE_DIRECTORY + "01.png";

		ImageModel createdImageModel = ImageModel.create(filename);

		long id = createdImageModel.id;

		assertNotNull(ImageModel.get(id).get());
	}

	@Test
	public void insert_3_images_expect_to_getAll_images() {
		ImageModel.create(ImageUploader.IMAGE_DIRECTORY + "01.png");
		ImageModel.create(ImageUploader.IMAGE_DIRECTORY + "02.png");
		ImageModel.create(ImageUploader.IMAGE_DIRECTORY + "03.png");

		int databaseSize = ImageModel.getAll().size();

		assertEquals(3, databaseSize);
	}

	@Test
	public void tag_image_expect_image_to_contain_one_tag() {
		String filename = ImageUploader.IMAGE_DIRECTORY + "01.png";
		String tagName = "tag";

		ImageModel imageModel = new ImageModel(filename);
		TagModel tagModel = TagModel.create(tagName);

		imageModel.addTag(tagModel);

		assertEquals(1, ImageModel.get(imageModel.id).get().tags.size());
	}

	@Test
	public void tag_image_twice_expect_image_to_contain_two_tags() {
		String filename = ImageUploader.IMAGE_DIRECTORY + "01.png";
		String tag1 = "tag1";
		String tag2 = "tag2";

		ImageModel imageModel = new ImageModel(filename);
		TagModel tagModel1 = TagModel.create(tag1);
		TagModel tagModel2 = TagModel.create(tag2);

		imageModel.addTag(tagModel1);
		imageModel.addTag(tagModel2);

		assertEquals(2, ImageModel.get(imageModel.id).get().tags.size());
	}
}