package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.FakeApplication;
import play.test.Helpers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;

public class ImageModelTest {
	public static FakeApplication application;

	@BeforeClass
	public static void startApp() {
		application = Helpers.fakeApplication();

		Helpers.start(application);
	}

	@AfterClass
	public static void stopApp() {
		Helpers.stop(application);
	}

	@Before
	public void dropCreateDb() {
		String serverName = "default";

		EbeanServer server = Ebean.getServer(serverName);
		ServerConfig config = new ServerConfig();

		DdlGenerator ddl = new DdlGenerator();
		ddl.setup((SpiEbeanServer) server, new H2Platform(), config);
		ddl.runScript(false, ddl.generateDropDdl());
		ddl.runScript(false, ddl.generateCreateDdl());
	}

	@Test
	public void addImageToDatabase() {
		String filename = "../../images/999.png";

		new ImageModel(filename).save();

		ImageModel imageModel = ImageModel.find.where()
				.eq("filename", filename).findUnique();
		
		int numberOfImagesInDatabase = ImageModel.getAll().size();

		assertEquals(1, numberOfImagesInDatabase);
		assertNotNull("imageModel is null", imageModel);
		assertEquals("filename property doesn't match", filename,
				imageModel.filename);
	}

	@Test
	public void createAndSaveImageToDatabase() {
		String filename = "../../images/999.png";

		ImageModel.create(filename);

		ImageModel imageModel = ImageModel.find.where()
				.eq("filename", filename).findUnique();
		
		int numberOfImagesInDatabase = ImageModel.getAll().size();

		assertEquals(1, numberOfImagesInDatabase);
		assertNotNull("imageModel is null", imageModel);
		assertEquals("filename property doesn't match", filename,
				imageModel.filename);
	}

	@Test
	public void getImageById() {
		String filename = "../../images/03.png";

		ImageModel createdModel = ImageModel.create(filename);
		ImageModel imageModel = ImageModel.get(createdModel.id);
		
		int numberOfImagesInDatabase = ImageModel.getAll().size();

		assertEquals(1, numberOfImagesInDatabase);
		assertNotNull(" imageModel is null", imageModel);
		assertEquals("filename property doesn't match", filename,
				imageModel.filename);
	}

	@Test
	public void getAllImages() {
		List<String> filenames = new ArrayList<>();

		filenames.add("../../images/901.png");
		filenames.add("../../images/902.png");
		filenames.add("../../images/903.png");

		for (String filename : filenames)
			ImageModel.create(filename);

		List<ImageModel> imageModels = ImageModel.getAll();

		int numberOfImagesInDatabase = ImageModel.getAll().size();

		assertNotNull("ImageModel list is null", imageModels);
		assertEquals(
				"Number of Images in the database does not match the number of files added",
				3, numberOfImagesInDatabase);
	}
}
