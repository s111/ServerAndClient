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
		String filename = "../../images/01.png";

		new ImageModel(filename).save();

		ImageModel imageModel = ImageModel.find.where()
				.eq("filename", filename).findUnique();

		assertNotNull(imageModel);
	}

	@Test
	public void insert_image_expect_database_size_1() {
		String filename = "../../images/01.png";

		new ImageModel(filename).save();

		int databaseSize = ImageModel.find.all().size();

		assertEquals(1, databaseSize);
	}

	@Test
	public void create_image_expect_to_retrieve_image() {
		String filename = "../../images/01.png";

		ImageModel.create(filename);

		ImageModel imageModel = ImageModel.find.where()
				.eq("filename", filename).findUnique();

		assertNotNull(imageModel);
	}

	@Test
	public void create_image_expect_to_get_image() {
		String filename = "../../images/01.png";

		ImageModel createdImageModel = ImageModel.create(filename);

		long id = createdImageModel.id;

		ImageModel retrievedImageModel = ImageModel.get(id);

		assertNotNull(retrievedImageModel);
	}

	@Test
	public void insert_3_images_expect_to_getAll_images() {
		ImageModel.create("../../images/01.png");
		ImageModel.create("../../images/02.png");
		ImageModel.create("../../images/03.png");

		int databaseSize = ImageModel.getAll().size();

		assertEquals(3, databaseSize);
	}
}
