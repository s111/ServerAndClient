package controllers;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.io.IOException;

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

public class ImageRaterTest {
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
	public void rate_image_1_with_default_rating_0_expect_rating_0() throws IOException {
		String filename = "../../images/01.png";
		
		ImageModel createdImageModel = ImageModel.create(filename);
		long id = createdImageModel.id;
		
		callAction(controllers.routes.ref.ImageRater.rate(id));

		assertEquals(0, createdImageModel.rating);
	}
}
