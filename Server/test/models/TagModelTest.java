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
import com.google.common.base.Optional;

import controllers.ImageUploader;

public class TagModelTest {
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
		assertEquals(0, TagModel.find.all().size());
	}

	@Test
	public void insert_tag_expect_to_retrieve_tag() {
		String name = "tagName";

		new TagModel(name).save();

		assertNotNull(TagModel.get(name));
	}

	@Test
	public void insert_tag_expect_database_size_1() {
		String name = "tagName";

		new TagModel(name).save();

		int databaseSize = TagModel.getAll().size();

		assertEquals(1, databaseSize);
	}

	@Test
	public void create_tag_expect_to_get_tag() {
		String name = "tagName";

		TagModel.create(name);

		assertNotNull(TagModel.get(name));
	}

	@Test
	public void insert_3_tags_expect_to_getAll_tags() {
		TagModel.create("tag1");
		TagModel.create("tag2");
		TagModel.create("tag3");

		int databaseSize = TagModel.getAll().size();

		assertEquals(3, databaseSize);
	}

	@Test
	public void tag_image_expect_tag_to_contain_one_image() {
		String filename = ImageUploader.IMAGE_DIRECTORY + "01.png";
		String tagName = "tag";

		ImageModel imageModel = new ImageModel(filename);
		TagModel tagModel = TagModel.create(tagName);

		imageModel.addTag(tagModel);

		Optional<TagModel> retrievedTagModel = TagModel.get(tagName);

		int numRows = 0;

		if (retrievedTagModel.isPresent()) {
			numRows = retrievedTagModel.get().images.size();
		}

		assertEquals(1, numRows);
	}

	@Test
	public void tag_image_twice_expect_tags_to_contain_one_image() {
		String filename = ImageUploader.IMAGE_DIRECTORY + "01.png";
		String tag1 = "tag1";
		String tag2 = "tag2";

		ImageModel imageModel = new ImageModel(filename);
		TagModel tagModel1 = TagModel.create(tag1);
		TagModel tagModel2 = TagModel.create(tag2);

		imageModel.addTag(tagModel1);
		imageModel.addTag(tagModel2);

		Optional<TagModel> retrievedTagModel1 = TagModel.get(tag1);
		Optional<TagModel> retrievedTagModel2 = TagModel.get(tag2);

		int numRows1 = 0;
		int numRows2 = 0;

		if (retrievedTagModel1.isPresent()) {
			numRows1 = retrievedTagModel1.get().images.size();
		}

		if (retrievedTagModel2.isPresent()) {
			numRows2 = retrievedTagModel2.get().images.size();
		}

		assertEquals(1, numRows1);
		assertEquals(1, numRows2);
	}

	@Test
	public void tag_two_images_expect_tag_to_contain_two_images() {
		String filename1 = ImageUploader.IMAGE_DIRECTORY + "01.png";
		String filename2 = ImageUploader.IMAGE_DIRECTORY + "02.png";

		String tagName = "tag";

		ImageModel imageModel1 = new ImageModel(filename1);
		ImageModel imageModel2 = new ImageModel(filename2);

		TagModel tagModel = TagModel.create(tagName);

		imageModel1.addTag(tagModel);
		imageModel2.addTag(tagModel);

		Optional<TagModel> retrievedTagModel = TagModel.get(tagName);

		int numRows = 0;

		if (retrievedTagModel.isPresent()) {
			numRows = retrievedTagModel.get().images.size();
		}

		assertEquals(2, numRows);
	}
}
