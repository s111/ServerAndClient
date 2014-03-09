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

		TagModel tagModel = TagModel.find.where()
				.eq("name", name).findUnique();

		assertNotNull(tagModel);
	}

	@Test
	public void insert_tag_expect_database_size_1() {
		String name = "tagName";

		new TagModel(name).save();

		int databaseSize = TagModel.find.all().size();

		assertEquals(1, databaseSize);
	}

	@Test
	public void create_tag_expect_to_retrieve_tag() {
		String name = "tagName";

		TagModel.create(name);

		TagModel tagModel = TagModel.find.where()
				.eq("name", name).findUnique();

		assertNotNull(tagModel);
	}

	@Test
	public void create_tag_expect_to_get_tag() {
		String name = "tagName";

		TagModel.create(name);

		TagModel retrievedTagModel = TagModel.get(name);

		assertNotNull(retrievedTagModel);
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
		
		imageModel.tag(tagModel);
		
		assertEquals(1, tagModel.images.size());
	}
	
	@Test
	public void tag_image_twice_expect_tags_to_contain_one_image() {
		String filename = ImageUploader.IMAGE_DIRECTORY + "01.png";
		String tag1 = "tag1";
		String tag2 = "tag2";
		
		ImageModel imageModel = new ImageModel(filename);
		TagModel tagModel1 = TagModel.create(tag1);
		TagModel tagModel2 = TagModel.create(tag2);
		
		imageModel.tag(tagModel1);
		imageModel.tag(tagModel2);
		
		assertEquals(1, tagModel1.images.size());
		assertEquals(1, tagModel2.images.size());
	}
	
	@Test
	public void tag_two_images_expect_tag_to_contain_two_images() {
		String filename1 = ImageUploader.IMAGE_DIRECTORY + "01.png";
		String filename2 = ImageUploader.IMAGE_DIRECTORY + "02.png";
		
		String tagName = "tag";
		
		ImageModel imageModel1 = new ImageModel(filename1);
		ImageModel imageModel2 = new ImageModel(filename2);
		
		TagModel tagModel = TagModel.create(tagName);
		
		imageModel1.tag(tagModel);
		imageModel2.tag(tagModel);
		
		assertEquals(2, tagModel.images.size());
	}
}