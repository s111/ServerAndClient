package controllers;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;
import models.ImageModel;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;

public class ImageDescriberTest {
	@Before
	public void startApp() {
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
	public void set_description_without_value_expect_null() {
		String filename = ImageUploader.IMAGE_DIRECTORY + "01.png";
		
		ImageModel createdImageModel = ImageModel.create(filename);
		long id = createdImageModel.id;
		
		callAction(controllers.routes.ref.ImageDescriber.describe(id));

		assertEquals(null, createdImageModel.description);
	}
}
